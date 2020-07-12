# toc
toc is a command line tool for working with **T**able **o**f **C**ontents files found in the game Star Wars Galaxies.

A toc file describes which tre file contains which files, such as iff, stf...

It's basically a lookup table, hence the name Table of Contents.

## Installation
The tool requires Java SE 13 or newer.

You can download the tool under Releases. 

### Globally available in shells
```shell script
$ sudo ln -s /path/to/toc.sh /usr/bin/toc
```

## Examples
Here are some examples of use cases for the tool.

### Searching for a file
We'll search for a file named something along the lines of transport_arriving.snd in sku0_client.toc.
```shell script
$ toc -i sku0_client.toc -p | grep "transport_arriving.snd"
voice/sound/voice_starport_transport_arriving.snd@bottom.tre
```

### Counting amount of entries in a toc file
We want to know how many files are in sku0_client.toc
```shell script
$ toc -i sku0_client.toc -p | wc -l
193475
```