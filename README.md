# Music-player-metadata-viewer-2020

Prerequisites:
-java 1.8 or higher, jlayer, mp3agic, tika-core

This program is divided into 2 jar files.
One has a graphical interface (GUI) and the other is executable with the terminal (CLI)

Using the CLI:
"java -jar CLI.jar"
To display the commands, "java -jar CLI.jar -h"

Using the GUI:
1. "java -jar GUI.jar"
2. A window will open, click on "Folder ..." to choose an XSPF file or an initial folder.
3. The drop-down list fills up and some information is displayed.

-You can now choose the mp3 file you want to open via the drop-down list
and see its metadata as well as its cover image.

-To play the file, press the "play" button.
To stop it, the "pause" button.

-To switch between light mode and dark mode, tap the sun / moon icon.

-To generate an XSPF playlist of the found mp3 files, press the button
"Generate a Playlist" then choose "Default" to include the whole list
or "Custom" to choose which mp3 files to include.
A new "playlist.xspf" file will then be created at the location of this .jar.
