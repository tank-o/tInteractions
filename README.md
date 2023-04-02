# tInteractions

**Disclaimer: This plugin is still in development and is not ready for production use**


tInteractions is a library for creating complex interactions with Citizens NPCs through the use of 
Citizens already existing trait system. It is designed to to easy to use purely through command line 
and offers developers the ability to register their own interaction types and requirements for use
in their own plugins. 

## Requirements
* Citizens Plugin

## Installation
1. Download the latest jar of tInteractions from the releases page
2. Place the jar in your plugins folder
3. Restart your server

## Traits

### Sequential Interaction Trait

A sequential interaction trait holds a series of interactions that must be completed in order. The player triggers the interaction
event by right-clicking the NPC. Once an interaction is finished, and the next interactions requirements are met,
the next interaction will occur.

### Menu Interaction Trait - _Still in Development_

A menu interactions trait holds a set of interactions, however in this case the user can choose which interaction to 
trigger and allows interactions to be repeated. An example of where this may be useful is if you want a player to be able
to open an NPC shop while also interacting in other ways with the NPC.

## Interaction Types

### Text Interaction

A text interaction is the most basic interaction type. It simply displays a message to the player when the interaction is triggered.
A list of messages can be provided and the interaction will either go through the list in order or randomly select a message
to send to the player. In the case of a random message, only one will be sent to the player before the interaction ends.
In the case of a sequential message, all messages will be sent to the player before the interaction ends.

### Command Interaction

A command interaction is an interaction that runs a command when triggered. The command can be run as the player or as 
the server.

### tQuests Integration



