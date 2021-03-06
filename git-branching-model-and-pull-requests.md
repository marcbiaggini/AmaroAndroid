# Git branching model

To manage branches in this project, we use a variation of the approach proposed on the post [A succesful git branching model](http://nvie.com/posts/a-successful-git-branching-model).

## The main branches

* `master`
* `dev`

The `master` branch is the main branch where the source code always reflects a *production-ready* state.

The `dev` branch is the main branch where the latest changes live on. Only non-breaking changes should be merged into it, so a beta release can be easily created from it.

No changes will be commited directly to these branches, they only receive changes by merging from other branches (by Pull Requests).

## Feature branches

* May branch off from:
    * `dev` or `release-*`
* Must merge back into:
    * `dev` or `release-*`
* Branch naming convention:
    * Anything except `master`, `dev`, `release-*` or `beta-*`; hyphen separated words only

Feature branches are used to develop new features, improvements or bugfixes. Usually, new features must have a feature branch from the `dev` branch, and it will be merged back into `dev`.

When a release branch is up (see below) and you are making improvements or bugfixes for it, you create a feature branch from the current `release-*` branch, and your changes will be merged back to it.

Feature branches are where you work on and commit changes for any new features, improvements or bugfixes. Tipically, this is the only branch type that receives direct commits - any other branch does not receive commits, they only get changes by merging.

## Release branches

* May branch off from:
    * `dev` or `master`
* Must merge back into:
    * `dev` and `master`
* Branch naming convention:
    * `release-<version_number>`

Release branches support preparation of a new production release. It's meant to to last-minute adjustments, bugfixing problems that arise from QA, determining version number and so on. By using a release branch, the `dev` branch is still ready to get fresh-new features from developers that are working on a future release.

A release branch is started when `dev` branch almost reflects the desired state of the new release. After creating this branch, some QA testing could be done and all last-minute changes will be merged to it.

When the release branch is finished and code is ready for deployment, the branch is merged back into `dev` and `master` branches, and a tag is created with the release version number.

### Hotfixes

When something goes wrong or a small feature must be added immediately, a hotfix release branch will be used. Technically, it's much like any release branch except it's branched off from `master` branch.

In the iOS app context, hotfixes necessity should be avoided at all costs, since it takes a (possibly large) number of days from app submission and its availability on App Store.

## Beta releases

When the team have to test the current stage of development, a beta release is created. To do this, simply use the latest commit on the `dev` branch, update build number (see below) and deploy your archive through beta-testing tools.

## Version and build number

iOS app version numbers mimics [semantic versioning](http://semver.org/), despite it's nature being very different from API's.

For this project, versions are defined as `MAJOR.MINOR.PATCH`, where

* `MAJOR` reflects major changes on app from a frontend perspective (new overall design).
* `MINOR` reflects new features added, without drastic overall design.
* `PATCH` contains only minor improvements and bugfixes.

To keep version number lean on App Store, `CFBundleShortVersionString` key always uses 2 or 3 digits. E.g., these versions are valid: `1.0`, `1.0.1` and these are invalid: `1.0.0`, `1.1.0`.

For internal control, a build number is used as the fourth digit appended to the version number, and used for `CFBundleVersion` key. So, for version `1.0`, build numbers `1.0.0.0` and `1.0.0.12` are valid build numbers.

### When should I increase build number?

For each deploy (for both production or beta), increase the build number once before deploying it and, after merging back changes to `dev`, increase build number again. This keeps bug/crash reporting for these releases well isolated from debug versions installed on developer's devices. For example:

* `1.0.0.0` is being developed
* `1.0.0.1` beta is launched
* `1.0.0.2` continues development
* `1.0.0.3` release is launched
* A crash report comes.
    * If build number ends with `.0` or `.2`, we know that it's from a device that has a debug version.
    * If it ends with `.1`, we know that it's a beta version tested by the team (it will be on Crashlytics/TestFlight).
    * If it ends with `.3`, we know that it's a production version (it will be on App Store).

## Tags

For each deployed production or beta release, a tag must be created to back up the exact state of that version.

For production releases, tags must be named with a preffix `v`, followed by the build number, e.g. `v1.0.2.2`.

For beta releases, tags must be named with a preffix `b`, followed by the build number, e.g. `b1.0.2.3`.

# Pull requests

Every change on source code will be made available on the `dev` or `release-*` branches through pull requests. After making your changes, push your branch to `origin` and create a PR targeting the corresponding branch.

**Important:** Before pushing your branch, update it with the latest changes from corresponding branch!!! This reduces drastically the complexity of merging conflicts.

When creating the PR, follow these guidelines:

* For Title field, write a few words description (it may reflect your branch name, if applicablec).
* For Description field, write a detailed description of the feature you've just implemented.
* For Reviewers field, put all current iOS developers.
* Keep "Close `branch` after the pull request is merged checked.

## PR sizes and frequency

To make code reviewers lives a bit easier, it's highly recommended that you avoid massive PR's. If you are working on a big feature, do some effort to break it into smaller pieces that can be merged into `dev` (and, of course, don't break it). A good measure is to break big features into daily-finishable branches, so you do no less than 1 PR per day.

## PR responses

Each code review comment must be marked as one of the following:

— Required (“This must be fixed before merge.”)
— Nice to have (“This should be fixed eventually.”) 
— Personal preference (“I would do this, but you don’t have to.”) 
— Question (“What does this do?”)

The comment type will be the always the first word inside a comment.