# Blankcrop

Blankcrop is a program that can automatically crop a PNG image based on the redundant transparent space it has. If an image has a transparent background or border that is unnecessarily large, blankcrop removes the extra space that doesn't have to be there.

# Requirements & using

### Requirements
* Java 19 or later

### Download and how to use

Download blankcrop from the releases page and run it with `java -jar blankcrop.jar`. Introduce a path to a PNG image to crop it.

# Build from source

You can compile Blankcrop with Yuuka:

```
yuuka package
```

### Install on your system (Linux, OpenBSD, NetBSD)
Run as root:
```
yuuka install
```
Or, if you have `~/.local/bin` in your $PATH, you can run as a user to install Blankcrop there.

### Install on your system (FreeBSD)
Run as user:
```
yuuka install
```
Or run as root to install at ``/usr/local/bin`
