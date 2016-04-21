# Layout naming

When adding images to xcassets, use these patterns for names.

- activity: `activity_<context>`
- content: `content_<context>`
- fragment: fragment_<context>`
- item: `item_<context>_<widget>`


Where:

- `context` is the screen or app area. If the asset is used all over the app, omit it.
- `widget` is the actual component thath will need an adapter to list a specific number of items. Make an effort to write it in a way that other developers can find it easily. Can be omitted if `context` is enough.

**Examples:**

- icons:
    - `activity_splash`
    - `activity_login`
    - `activity_main`

- content:
    - `content_splash`
    - `content_login`
    - `content_main`

- fragment:
    - `fragment_splash`
    - `fragment_login`
    - `fragment_main`

- item:
    - `item_product_listview`
    - `item_favorites_gridview`
