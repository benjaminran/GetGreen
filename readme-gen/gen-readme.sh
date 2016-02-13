#!/bin/bash

DEST=../README.md

TITLE=../media/listing/title
SHORTDESCRIPTION=../media/listing/short-description
FULLDESCRIPTION=../media/listing/full-description

sed "s|\[//\]: # (title)|`cat ${TITLE}`|" README.md | \
    sed "s|\[//\]: # (short-description)|`cat ${SHORTDESCRIPTION}`|" README.md | \
    sed "s|\[//\]: # (full-description)|`cat ${FULLDESCRIPTION}`|" README.md | \
    sed "s|\[//\]: # (full-description)|`cat ${FULLDESCRIPTION}`|" README.md > ${DEST}
