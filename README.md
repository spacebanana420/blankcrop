# Blankcrop

Blankcrop is a program that can automatically crop a PNG image based on the redundant transparent space it has, as well as convert it to a different color palette. If an image has a transparent background or border that is unnecessarily large, blankcrop removes the extra space that doesn't have to be there.

## Requirements & running

### Requirements
* Java 19 or later

### Download and how to use

Download blankcrop from the [releases page](https://github.com/spacebanana420/blankcrop/releases) and run it with `java -jar blankcrop.jar`. Introduce a path to a PNG image and use the argument `-crop` to autocrop it.

## Palette file format

Blankcrop palette files are plain text files with the `.plt` file extension.

[Palette format documentation](palette.md)

## Build from source

You can compile Blankcrop with [Yuuka](https://github.com/spacebanana420/yuuka):

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
Or run as root to install at `/usr/local/bin`
