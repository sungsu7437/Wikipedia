# Wikipedia
This project is analysis Wikipedia dump data by Hadoop and get which title the users most interested in.

Here is input.txt which format is <key : title value : timestamps>  10GB
https://drive.google.com/file/d/0B5XVp1-R4ay2Zm14N2lWNi1zekk/view?usp=sharing

and output.zip which foramt is <key : title + "\t" + year + month  value : count>  4GB
https://drive.google.com/file/d/0B5XVp1-R4ay2RXpuOHhpZFgtSTg/view?usp=sharing

Clone this repository

run hadoop WikiProject.java with 10GB dump data

move result file of the hadoop (4GB one) to /wordcloud

run wordcloud (ex : 'python ./wordcloud/simple.py 201103')
