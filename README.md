# au_software_design_autumn_2016

## A repo for Software Design hometasks, fall 2016

### The description of CLI
The main utility is the CLI class, which reads the input line by line until it gets the 'exit' command. Each line is passed to Processor, where it is syntactically parsed by Parser (that also substitutes environmental variables using the environment provided by Processor) and then interpreted by Processor itself. The boolean result is then returned to the main CLI, indicating either the 'exit' command was performed or not.
