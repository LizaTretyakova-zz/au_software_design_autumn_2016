# au_software_design_autumn_2016

## A repo for Software Design hometasks, fall 2016

### The description of CLI
The main utility is the CLI class, which reads the input line by line until it gets the 'exit' command. Each line is passed to Processor, where it is syntactically parsed by Parser (that also substitutes environmental variables using the environment provided by Processor) and then interpreted by Processor itself. The boolean result is then returned to the main CLI, indicating either the 'exit' command was performed or not.

### A note on how the JCommander was chosen
I googled "java command line parsing libraries", checked the replies for this question on StackOverflow
(second search result) and picked the one which was easiest to understand and to start using
(JCommander has a very nice description page with a table of contents and lots of self-explanatory examples).