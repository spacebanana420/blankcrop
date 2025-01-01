# Blankcrop palette format

To convert a sprite from one palette to another, you need to pass 2 palette files as arguments. The first file is used as reference, and the second file is used for conversion.

The palette format is very simple. It's a plain text format with the file extension `.plt` composed of RGBA values representing colors. Each color can be separated by line, or you can combine multiple colors into a single line, as long as all the 4 RGBA channels are in the same line.

Here's an example of a palette file's contents:

```
255 222 206 255
255 173 140 255
206 99 74 255
132 49 0 255
255 206 222 255
247 156 222 255
222 82 148 255
165 33 107 255
148 181 255 255
90 123 255 255
82 82 255 255
66 49 255 255
255 33 16 255
231 33 0 255
165 24 0 255
```

Each line is representing a color in the palette, composed of the red, green, blue and alpha channels' values.

To convert an image from this palette to another, you must have another palette with an equal amount of colors. The order of the colors in the files determines which is the equivalent color to convert from one palette to another.

You can also omit the alpha channel value if you want to assume it's always 255:

```
255 222 206
255 173 140
206 99 74
132 49 0
255 206 222
247 156 222
222 82 148
165 33 107
148 181 255
90 123 255
82 82 255
66 49 255
255 33 16
231 33 0
165 24 0
```

Note that, by omitting the alpha value, you cannot add multiple colors per line, since the supposed red channel of the next color will be read as the alpha channel of the previous.
