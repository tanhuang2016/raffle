# Raffle-fx: an application choosing a random name/number from a list

A JavaFX application for shuffling a list of Strings to choose a winner participant.

## How it works
If the participant in present, they are congratulated.
If not, the name is removed from the list, which gets shuffled again to choose another name.

There's an option of masking emails: in this case, only the part before the at sign will be displayed on the screen.
Example:
john.doe*****

You can also choose the duration of the raffle: 5, 10, or 15 seconds (default 15 s).

## How to build it
This application is available as a native executable for Linux, Windows, and macOS: https://github.com/code-with-bellsoft/raffle/releases/tag/main

The native executable doesn't require a JVM to run and can be launched as is.

To make changes to this application, you need to install a JDK with FX bundle, e.g., with SDKMAN:

```bash
sdk install java 21.0.8.fx-librca
```

To build a native executable, download Liberica Native Image Kit with FX:

```bash
sdk install java 23.1.8.fx-nik
```

Set the JAVA_HOME (or NIK_HOME) to Liberica NIK. Build the executable JAR file. Then, run the tracing agent, to collect metadata:

```bash
$NIK_HOME/bin/java -agentlib:native-image-agent=config-output-dir=./agent-data -jar target/raffle-1.0-SNAPSHOT.jar
```

When the application starts, run it through all execution paths.

At this point, you need to manually add the toolbar.fxml to the generated resources as the Tracing Agent doesn't pick it up:

```json
{
  "pattern": "\\Qfxml/toolbar.fxml\\E"
}
```

Then, run

```bash
mvn clean package -Pnative -DskipTests
```

Finally, you can run the native image as any executable:

```bash
target/native/raffle
```

A more detailed description of combining JavaFX and GraalVM Native Image can be fount in this article: https://bell-sw.com/blog/how-to-create-javafx-native-images/