# Raffle-fx: an application choosing a random name/number from a list

A JavaFX application for shuffling a list of Strings to choose a winner participant.

If the participant in present, they are congratulated.
If not, the name is removed from the list, which gets shuffled again to choose another name.

There's an option of masking emails: in this case, only the part before the at sign will be displayed on the screen.
Example:
john.doe*****

You can also choose the duration of the raffle: 5, 10, or 15 seconds (default 15 s).

This application is available as a native executable for Linux, Windows, and macOS: https://github.com/code-with-bellsoft/raffle/releases/tag/main

The native executable doesn't require a JVM to run and can be launched as is.

To make changes to this application, you need to install a JDK with FX bundle, e.g., with SDKMAN:

```bash
sdk install java 21.0.8.fx-librca
```