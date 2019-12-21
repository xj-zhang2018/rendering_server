    $(document).ready(function(){  
      linechart("queryChartData", "#container");
      piechart("queryNodesPieChartData", "#container1"); 
	  linechart_CPU("queryWebServerCPU","#container2");
	  barchart_MEM("queryWebServerMEM","#container3");
    });  
         
    /* 
     * 曲线图 
     * 参数：action：调用的action函数 
     *       div：曲线图插入的页面 
     *       title：曲线图的名称 
     *       ytitle：曲线图y轴的名称 
     *       dat: 传入数据 
     */  
    function linechart(action,div ) {
    	
         $.post(action, function(data){

        	 $(function () {
        		    
        	        $(div).highcharts({
        	        	chart: {
        		            height: 300,
        		        },
        	            title: {
        	                text: '作业情况统计分析',
        	                x: -20 //center
        	            },
        	            subtitle: {
        	                text: '最近一周',
        	                x: -20
        	            },
        	            xAxis: {
        	                categories: data.strEndTime,
							labels: {
								style: {
									color: 'red',
									fontSize:'10px'
								}
							}
        	            },
        	            yAxis: {
        	            	min: 0,
        	                title: {
        	                    text: '个数(个)'
        	                },
        	                plotLines: [{
        	                    value: 0,
        	                    width: 1,
        	                    color: '#808080'
        	                }]
        	            },
        	            tooltip: {
        	                valueSuffix: '个'
        	            },
        	            legend: {
							layout: 'vertical',
							backgroundColor: 'white',
							align: 'left',
							verticalAlign: 'top',
							y: 60,
							x: 70,
							borderWidth: 1,
							borderRadius: 0,
							title: {
								text: ':: 拖动我'
							},
							floating: true,
							draggable: true,
							zIndex: 20
        	            },
        	           
        	            series: [{
        	                name: '作业提交数量',
        	                data: data.jobCreateNums
        	            }, {
        	                name: '作业开始数量',
        	                data: data.jobStartNums
        	            }, {
        	                name: '作业完成数量',
        	                data: data.jobEndNums
        	            }]
        	        });
        	    });
             });  
          
    };  
    
    /* 
     * 节点状态圆饼图
     * 参数：action：调用的action函数 
     *       div：曲线图插入的页面 
     *       title：曲线图的名称 
     *       ytitle：曲线图y轴的名称 
     *       dat: 传入数据 
     */  
    function piechart(action,div ) {  
         $.post(action, function(data){  
        	 $(function () {
        		    $(div).highcharts({
        		    	
        		        chart: {
							height: 300,
        		            plotBackgroundColor: null,
        		            plotBorderWidth: null,
        		            plotShadow: false
        		        },
        		        colors:[
        		                'yellow',
        		                'green',
        		                'gray'
        		        ],
        		        title: {
        		            text: '节点情况统计分析'
        		        },
						subtitle: {
							text: '节点总数：'+(data.busyNodes+data.freeNodes+data.downNodes)+'个'
						},
        		        tooltip: {
        		    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',							
							formatter: function(){
								return this.point.name+'数目：'+'<b>'+Highcharts.numberFormat((data.busyNodes+data.freeNodes+data.downNodes)*this.point.percentage/100,0)+'个</b>';
							}
        		        },
        		        plotOptions: {
        		            pie: {
								size:'100%',							
        		                allowPointSelect: true,
        		                cursor: 'pointer',
								
        		                dataLabels: {
        		                    enabled: true,
        		                    color: '#000000',
									distance: -50,
        		                    connectorColor: '#000000',
        		                    formatter: function() {
      		        				  if (this.percentage > 0)
      		                              return '<b>' + this.point.name + '</b>: ' + Highcharts.numberFormat(this.percentage,2)+ ' %';
      		        				
        		                    }
        		                    /*format: '<b>{point.name}</b>: {point.percentage:.1f} %'*/
        		                }
        		            }
        		        },
        		       
        		        series: [{
        		            type: 'pie',
        		            name: '所占百分比',
        		            data: [
        		               
        		                {
        		                    name: '忙碌节点',
        		                    y: data.busyNodes,
        		                    sliced: true,
        		                    selected: true
        		                },
        		                ['空闲节点',   data.freeNodes],
        		                ['宕机节点',   data.downNodes]
        		                
        		            ]
        		        }]
        		    });
        		});
        		    

             });  
          
    };
	
	 /* 
     * 管理节点CPU利用率曲线图 
     */  
    function linechart_CPU(action,div) {
		var chart1;
	    Highcharts.setOptions({
           global: {
             useUTC: false
           }
        });
        //声明报表对象
		chart1 = new Highcharts.Chart({
			 chart: {
				height: 310,
				renderTo: 'container2',
				defaultSeriesType: 'spline',
				marginRight: 10
			 },
			 title: {
				text: 'CPU使用率动态曲线图'
			 },
            xAxis: {
              title: {
                text: '时间'
              },
              type: 'datetime',
              tickPixelInterval: 150
            },
            yAxis: {
              title: {
                text: '使用率'
              },
			  max: 100,
			  min: 0,
			  tickInterval: 25,
              //指定y=3直线的样式
              plotLines: [
                {
                  value: 0,
                  width: 1,
                  color: '#808080'
                }
              ]
            },
			tooltip: {
              formatter: function() {
                return '<b>' + this.series.name + '</b><br/>' +
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                        Highcharts.numberFormat(this.y, 4);
              }
            },
			legend: {
              enabled: true
            },
			exporting: {
              enabled: true
            },
			series: [{
                name: 'CPU使用率',
                data: (function() {
                  // 初始化数据
                  var data = [],
                          time = (new Date()).getTime(),
                          i;
                  for (i = -19; i <= 0; i++) {
                    data.push({
                      x: time + i * 1000,
                      y: Math.random() * 100
                    });
                  }
                  return data;
                })()
			}]
      });
	  function getCpuInfo(){
	        $.ajax({
	            url: "queryWebServerCPU",
	            type: "post",
	            dataType:'json',
	            success: function(data){
	            chart1.series[0].addPoint([(new Date()).getTime(),data.y], true, true);
	            }
	        });
	  }
	  setInterval(getCpuInfo, 2000);

	};
	
	 /* 
     * 管理节点内存柱状图 
     */  
    function barchart_MEM(action,div) {
		var chart2;

        //声明报表对象
		chart2 = new Highcharts.Chart({
			chart: {
				height: 310,
				renderTo: 'container3',
	            type: 'column'
	        },
	        title: {
	            text: '内存占用'
	        },
	        xAxis: {
	            categories: ['内存']
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: 'Gigabytes'
	            },
	            tickInterval: 2,
	            stackLabels: {
	                enabled: false,
	                style: {
	                    fontWeight: 'bold',
	                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
	                }
	            }
	        },
	        tooltip: {
	            formatter: function() {
	                return '<b>'+ this.x +'</b><br>'+
	                    this.series.name +': '+ this.y +'<br>'+
	                    'Total: '+ Highcharts.numberFormat(this.point.stackTotal,2);
	            }
	        },
	        plotOptions: {
	            column: {
	                stacking: 'normal',
	                dataLabels: {
	                    enabled: true,
	                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
	                }
	            }
	        },
	        series: [{
	            name: '剩余',
	            data: [5]
	        }, {
	            name: '已占用',
	            data: [3]
	        }]
		});
		
		function getMemInfo(){
			
		      $.ajax({
		          url: "queryWebServerMEM",
		          type: "post",
		          dataType:'json',
		          success: function(data){
		        	  chart2.series[0].setData([data.freeMem]);
		        	  chart2.series[1].setData([data.busyMem]);
		          }
		      });
		}
		setInterval(getMemInfo, 2000);

	};	
	
	
	