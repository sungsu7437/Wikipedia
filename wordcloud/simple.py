#!/usr/bin/env python2
"""
Minimal Example
===============
Generating a square wordcloud from the US constitution using default arguments.
"""

import sys
from os import path
from wordcloud import WordCloud

d = path.dirname(__file__)

# Read the whole text.
time = sys.argv[1]
year = time[:4]
fileName = "wiki" + year + ".txt"

print "Reading Wikipedia file"
text = open(path.join(d, fileName)).read()
text = time + "\n" + text

# Generate a word cloud image
print "start to generate word cloud"
wordcloud = WordCloud().generate(text)

# Display the generated image:
# the matplotlib way:
import matplotlib.pyplot as plt
plt.imshow(wordcloud)
plt.axis("off")

# take relative word frequencies into account, lower max_font_size
wordcloud = WordCloud(max_font_size=40, relative_scaling=.5).generate(text)
plt.figure()
plt.imshow(wordcloud)
plt.axis("off")
plt.show()

# The pil way (if you don't have matplotlib)
#image = wordcloud.to_image()
#image.show()
