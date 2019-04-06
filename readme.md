# Simple accounting android app

## Brief Introduction
In this app, users can set their budget, add, edit, delete an entry, and show how much money they spent and left. Warning amount can also be set to get reminder if the amount of money is near their budget.

## How to use?
+ For the first time, the app will prompt a dialog requesting user to set their budget and warning amount after they can start using.
+ Add: User can click the "Add" button to add en entry.
+ Edit: Click "Expense" button and you can see a list of items you added previously. Click on the item you want to edit, and the dialog will be prompted.
+ Delete: Click the button on the top-left corner in the "Expense" screen to enter selection mode, and then select items you want to delete. For the current version, selected entries **will be deleted immediately.**

## Implementation details
1. Information storing
For each entry, sqlite database file stored in internal storage is used. For user settings, regular text file is used. When users close the app, the two numbers shown in the main screen are stored to a text file, so next time when users reopen the app, the two number can be gotten from this file rather than making query to database. 
2. 