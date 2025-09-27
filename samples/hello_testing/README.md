# Overview

A sample app that has a "SAY HELLO" button.  When clicked the text view will change from
"Ready" to "Clicked!".

There is also a "GO TO DETAIL" button that will navigate to a detail screen that says 
"Welcome, Test Student"

# Screenshots

## Main Screen
The main activity screen

![Main Screen](../../images/hello_testing/main_screen.png)

## Clicking Say Hello
After clicking the "SAY HELLO" button

![Clicked Say Hello](../../images/hello_testing/after_click.png)

## Detail Screen
The detail activity screen

![Detail Screen](../../images/hello_testing/go_to_detail.png)

# Test Results

Running MainActivityTest

All the tests are successful

![Test Results](../../images/hello_testing/hello_testing_good.png)

After changing one of the tests to expect a different string, the test fails

![Test Results](../../images/hello_testing/hello_testing_fail.png)