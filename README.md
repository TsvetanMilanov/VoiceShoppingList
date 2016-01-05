# VoiceShoppingList
Android app for quick shopping list creation

## Business problem
The app allows easy and fast creation and editing of shopping lists by voice commands. All you need is just one hand to hold the phone and instruct it what product to mark as bought, add new one to the list or remove unnecessary ones. This allows fast edition of previously made lists on the fly while shopping at the market.

## Software requirements

### Phase 1

- create single shopping list by voice commands, stored in ram, no permanent list saving yet

- products should be numbered in the list

        1. cucumber
        2. pineapple
        3. ....

- voice commands

    - **add cucumber** - adds a new product 'cucumber' to the end of the list

    - **remove one** - removes the first product from the list

    - **check one** - marks first product as bought

    - **clear** - removes all products from the list

### Phase 2

- allow creation of multiple shopping lists and store them locally in SQLite

- shopping lists have name and when listed should be displayed along with the date of creation so repeating names are allowed. The database will generate unique ids.

- voice commands

    - **list show** - shows all lists as numbered list along with their names and date of creation

    - **add Groceries** - creates a new list called Groceries and opens it

    - **remove one** - deletes first shopping list from the list

    - **open one** - opens the first shopping list from the list

### Phase 3

- allow synchronization of data with remote db

- allow adding products cost, if not specified defaults to 0

- voice commands

    - **cost one thirthy** - adds 30 units of cost to product 1

- allow statistics about total money spent per month, per product and so on, that are fetched from the server

### Phase 4

- allow autofilling product cost with last added cost for same product name

- allow product weight and cost per weight addition
