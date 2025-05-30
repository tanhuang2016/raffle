# Raffle-fx: an application choosing a random name/number from a list

A JavaFX application for shuffling a list of Strings to choose a winner participant.
If the participant in present, they are congratulated.
If not, the name is removed from the list, which gets shuffled again to choose another name.

There's an option of masking emails: in this case, only the part before the at sign will be displayed on the screen.
Example:
john.doe*****

To make changes to this application, you need to install a JDK with FX bundle, for instance, Liberica JDK.

E.g., with SDKMAN:
```bash
sdk install java 21.0.6.fx-librca
```
