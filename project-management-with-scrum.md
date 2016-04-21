# Project Management with Scrum

Scrum is a management framework for developing and sustaining complex products. It's very well established in software industry and gives us good ways to keep track of each developer's tasks and to estimate with optimal accuracy by use of story points.

A summary of Scrum concepts will be discussed below, but reading [this reference](https://www.scrumalliance.org/why-scrum/scrum-guide) is highly recommended.

# Scrum main concepts

The Scrum framework consists of Scrum Teams and their associated **roles**, **events**, **artifacts**, and **rules**.

Each component within the framework serves a specific purpose and is essential to Scrum’s success and usage. The rules of Scrum bind together the events, roles, and artifacts, governing the relationships and interaction between them.

## Scrum roles

### Product Owner

The Product Owner is the sole person responsible for managing the Product Backlog. Product Backlog management includes:

- Clearly expressing Product Backlog items;
- Ordering the items in the Product Backlog to best achieve goals and missions;
- Optimizing the value of the work the Development Team performs;
- Ensuring that the Product Backlog is visible, transparent, and clear to all, and shows what the Scrum Team will work on next; and,
- Ensuring the Development Team understands items in the Product Backlog to the level needed.

The Product Owner of Customer-App project is Sara Kassatly.

### Development Team

The Development Team consists of professionals who do the work of delivering a potentially releasable Increment of “Done” product at the end of each Sprint.

Development Teams have the following characteristics:

- They are self-organizing. No one (not even the Scrum Master) tells the Development Team how to turn Product Backlog into Increments of potentially releasable functionality;
- Development Teams are cross-functional, with all of the skills as a team necessary to create a product Increment;
- Scrum recognizes no titles for Development Team members other than Developer, regardless of the work being performed by the person; and,
- Scrum recognizes no sub-teams in the Development Team, regardless of particular domains that need to be addressed like testing or business analysis.

### Scrum Master

The Scrum Master is responsible for ensuring Scrum is understood and enacted. Scrum Masters do this by ensuring that the Scrum Team adheres to Scrum theory, practices, and rules.

#### Scrum Master Service to the Product Owner

The Scrum Master serves the Product Owner in several ways, including:

- Finding techniques for effective Product Backlog management;
- Helping the Scrum Team understand the need for clear and concise Product Backlog items;
- Understanding product planning in an empirical environment;
- Ensuring the Product Owner knows how to arrange the Product Backlog to maximize value;
- Understanding and practicing agility; and,
- Facilitating Scrum events as requested or needed.

#### Scrum Master Service to the Development Team

The Scrum Master serves the Development Team in several ways, including:

- Coaching the Development Team in self-organization and cross-functionality;
- Helping the Development Team to create high-value products;
- Removing impediments to the Development Team’s progress;
- Facilitating Scrum events as requested or needed; and,
- Coaching the Development Team in organizational environments in which Scrum is not yet fully adopted and understood.

#### Scrum Master Service to the Organization

The Scrum Master serves the organization in several ways, including:

- Leading and coaching the organization in its Scrum adoption;
- Planning Scrum implementations within the organization;
- Helping employees and stakeholders understand and enact Scrum and empirical product development;
- Causing change that increases the productivity of the Scrum Team; and,
- Working with other Scrum Masters to increase the effectiveness of the application of Scrum in the organization.

The Scrum Master of Customer-App is Luciano Sugiura.

## Scrum events

There are pre-determined events in Scrum, which are meant to catch problems that arise during development and discuss improvements at an very early stage. Using **only** the pre-determined events makes additional time-consuming meetings unnecessary, which potencially deviates attention from developers and slows down the development.

### The Sprint

The heart of Scrum is a Sprint, a time-box of one month or less (in our case, one week) during which a “Done”, useable, and potentially releasable product Increment is created.

During the Sprint:

- No changes are made that would endanger the Sprint Goal;
- Quality goals do not decrease; and,
- Scope may be clarified and re-negotiated between the Product Owner and Development Team as more is learned.

### Sprint Planning

The work to be performed in the Sprint is planned at the Sprint Planning. This plan is created by the collaborative work of the entire Scrum Team. The Scrum Master ensures that the event takes place and that attendants understand its purpose.

Participates in the Sprint Planning: the Product Owner, Scrum Master, Development Team and other stakeholders, if applicable.

In a Sprint Planning:

- The Product Owner discusses the objective that the Sprint should achieve and the Product Backlog items that, if completed in the Sprint, would achieve the Sprint Goal;
- The stories are expected to be doable in the Sprint. The concept of doable will be first discussed with the Product Owner and the Scrum Master;
- Stories selected to be done in the sprint can be broken down into smaller ones if needed;

(From this point, only Scrum Master and Development Team participate)

- The Development Team estimates how many points each task will take, through [Planning Poker](#markdown-header-story-points-and-planning-poker);
- The sum of the points for the Sprint will show if the Stories will be really doable during the Sprint time. If the number of points are greater than the team velocity, more Stories must be added; on the other hand, if the number of points are lesser than the team velocity, Stories must be removed. This is done by the Product Owner;
- The **team velocity** is the number of Story Points that can be done in a Sprint. It will be determined after a few number of Sprints and will guide estimation of further Sprints.

### Daily Scrum

The Daily Scrum is a time-boxed event for the Development Team to synchronize activities and create a plan for the next 24 hours. During the meeting, the Development Team members explain:

- What did I do yesterday that helped the Development Team meet the Sprint Goal?
- What will I do today to help the Development Team meet the Sprint Goal?
- Do I see any impediment that prevents me or the Development Team from meeting the Sprint Goal?

Participates in the Daily Scrum: Scrum Master and Development Team.

### Sprint Review

After a Sprint ends, a Sprint Review meeting is set.

- The Development Team discusses what went well during the Sprint, what problems it ran into, and how those problems were solved;
- The entire group collaborates on what to do next, so that the Sprint Review provides valuable input to subsequent Sprint Planning;
- The result of the Sprint Review is a revised Product Backlog that defines the probable Product Backlog items for the next Sprint.

Participates in the Sprint Review: Product Owner, Scrum Master and Development Team.

## Product Backlog

The Product Backlog is an ordered list of everything that might be needed in the product and is the single source of requirements for any changes to be made to the product. The Product Owner is responsible for the Product Backlog, including its content, availability, and ordering.

## Definition of "Done"

When a Product Backlog item or an Increment is described as “Done”, everyone must understand what “Done” means. Although this varies significantly per Scrum Team, members must have a shared understanding of what it means for work to be complete, to ensure transparency.

The definition of "Done" helps developers to determine exactly when a Story is finished, as well as how many points a Story would take, during the Sprint Planning.

For Customer-App, a Story is considered "Done" when the described feature is fully implemented, its source code well documented, with corresponding unit tests and UI tests (when applicable), and the pull request regarding the feature is approved. Only after these requirements the Story can be moved from "In Progress" to "Done" status on JIRA, so Product Owner and QA can check what was done.

## Story Points and Planning Poker

When defining how many Story Points a Story will take, we use the Planning Poker technique.

The reason to use Planning Poker is to avoid the influence of the other participants. If a number is spoken, it can sound like a suggestion and influence the other participants' estimation. Planning poker should force people to think independently and propose their numbers simultaneously. This is accomplished by requiring that all participants tell their estimating points at the same time.

### The unity Story Point

A unity of a Story Point must be based on an Story example, and every developer must have the same complexity base in order to estimate other Stories.

The unity Story we will use is:

"Given an already implemented and working login screen with e-mail and password fields, add a confirmation password field, adjust layout with required margins, enhance methods that accomodate this field and write/adjust tests for the new implementation."

Note that the time it costs is not the goal - measure the complexity of implementing the Story intead.

### Possible points

The possible Story Points are a typically a Fibonacci sequence with zero.
We will use 0, 1/2, 1, 2, 3, 5, 8, 13, 21, ?, where "?" means it's totally unpredictable and 0 means it can done immediately (like fixing a typo string).

The reason for using the Fibonacci sequence is to reflect the inherent uncertainty in estimating larger items.

### Procedure

- Each task's scope is discussed by the Developer Team;
- After scope consensus, each developer estimates how many points the task will take;
- The developer who have an estimate tells that he's ready;
- When every developer is ready, the whole team tells each estimation points;
- If any developer gives an "?", the Story must be broken down into smaller ones;
- If all estimation points are equal, it is used;
- If one or more points diverge, developers discuss about why it should be bigger os smaller, then a new round (think, say is ready, tell) occurs. This repeats until the every developer gives the same points.

## The Burndown Chart

![Burndown chart example](http://atlassian.wpengine.netdna-cdn.com/wp-content/uploads/jira_retrospective_burndown1.png)

The Burndown Chart shows:

- The time of a Sprint on X-axis
- How many Story Points are assigned to that Sprint on Y-axis
- The expected progress (Guideline curve)
- The current progress (Remaining Values curve)

It's an important tool to measure if the number of Story Points are well suited to the Sprint duration.