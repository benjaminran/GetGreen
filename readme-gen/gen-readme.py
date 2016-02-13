#!/usr/bin/env python3
import re

source='readme-source.md'
title='../media/listing/title'
shortdescription='../media/listing/short-description'
fulldescription='../media/listing/full-description'

def tag(s):
    return str.format('\[//\]: # \({}\)', s)

def readfile(f):
    return open(f, 'r').read()

readme = open(source, 'r').read()
readme = re.sub(tag('title'), '# '+readfile(title), readme)
readme = re.sub(tag('short-description'), readfile(shortdescription), readme)
readme = re.sub(tag('full-description'), readfile(fulldescription),readme)
print(readme)
