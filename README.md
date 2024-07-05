# WarpS JFiles Extension

"Just" LOAD and "Just" STORE support

## Install

`./gradlew shadowJar`

Copy the build/libs/*.jar to your warp/lib folder,

In `70--extensions.conf` add `warpscript.extension.jfiles = io.warp1.script.ext.jfiles.JFilesWarpScriptExtension `