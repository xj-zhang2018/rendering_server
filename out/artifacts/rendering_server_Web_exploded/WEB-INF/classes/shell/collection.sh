#!/bin/bash 
 
vilidatePreType(){
  fileName=$1
  isPre=${fileName#*_}
  if [ "$isPre" = "pre" ]
  then
       return 1
  else
       return 0
  fi
     }

  for element in `ls $1`
       do  
           dir_or_file=$1"/"$element
           filename=`echo $dir_or_file | cut -d \. -f 1`
           formate=`echo $dir_or_file | cut -d \. -f 2`
           if [ -f $dir_or_file ]
           then 
               if [ "$formate" == "png" ]
               then
                  echo "图片是"$dir_or_file
                  vilidatePreType $filename
                  resType=$?
                 # echo "文件名结尾类型是"$resType
                  if [ "$resType" -eq 0 ]
                  then
                    mv $dir_or_file $1/Pictures
                  fi
               fi 
           fi
       done    

echo "收集图片结果任务已经完毕"


