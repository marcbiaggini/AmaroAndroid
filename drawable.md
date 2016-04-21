# Drawable naming

When adding images to drawable folder, use these patterns for names.

- icons: `ic_<context>_<description>`
- backgrounds: `bg_<context>_<description>`
- button icons: `btn_ic_<context>_<description>`
- button backgrounds: `btn_bg_<radius>_<color>`
- pixel images: `pixel_<color>`

Where:

- `context` is the screen or app area. If the asset is used all over the app, omit it.
- `description` is the actual usage of the asset. Make an effort to write it in a way that other developers can find it easily. Can be omitted if `context` is enough.
- `radius` is the button corner radius.
- `color` if the asset can have many versions of colors, use it on name. Here colors are not the color names, but its hexadecimal representations.

**Examples:**

- icons:
    - `ic_tab_bar_home`
    - `ic_tab_bar_notifications`
    - `ic_login_rockspoon_logo`

- backgrounds:
    - `bg_splash`
    - `bg_listing_carousel`

- button icons:
    - `btn_ic_login_spoon`
    - `btn_ic_login_facebook`
    - `btn_ic_login_google`

- button backgrounds:
    - `btn_bg_login_facebook`
    - `btn_bg_0_ffffff`
    - `btn_bg_5_00ff00`
