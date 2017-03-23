# Wikipedia
This project is analysis Wikipedia dump data by Hadoop and get which title the users most interested in.

Here is input.txt which format is <key : title value : timestamps>  10GB
https://drive.google.com/open?id=0B0k1asiqBohoTEd4RDlqLWRLNzg

and output.zip which foramt is <key : title + "\t" + year + month  value : count>  4GB

https://drive.google.com/open?id=0B0k1asiqBohoaE1wb3pVZTFCRFE

Clone this repository

run hadoop WikiProject.java with 10GB dump data

move result file of the hadoop (4GB one) to /wordcloud

run wordcloud (ex : 'python ./wordcloud/simple.py 201103')
