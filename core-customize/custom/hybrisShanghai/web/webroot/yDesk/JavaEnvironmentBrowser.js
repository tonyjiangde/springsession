Ext.require('Ext.chart.*');
Ext.require(['Ext.layout.container.Fit', 'Ext.window.MessageBox']);
Ext.require([
             'Ext.grid.*',
             'Ext.data.*',
             'Ext.util.*',
             'Ext.tip.QuickTipManager',
         ]); 

Ext.define('urlsmpdel', {
    extend: 'Ext.data.TreeModel',
    fields: [
      
        { name: 'text', type: 'string'},
        {name: 'filename', type:'string'},
        {name: 'duplicated', type:'boolean'}
      /*  { name: 'leaf', type: 'boolean', mapping: 'leaf' },
        { name: 'loaded', type: 'boolean', mapping: 'loaded', defaultValue: false },
        { name: 'expanded', defaultValue: true }*/
    ]
});







Ext.define('hybrisDesktop.JavaEnvironmentBrowser', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.data.ArrayStore',
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id: 'JavaEnvironmentBrowser-win',

    init: function() {
        this.launcher = {
            text: 'JavaEnvironmentBrowser',
            iconCls: 'icon-java'
        };
    },
    qtip: function(value, metaData,record, rowIdx,colIdx,store){
        value = Ext.String.htmlEncode(value);
        metaData.tdAttr='data-qtip="<xmp>'+Ext.String.htmlEncode(value)+'</xmp>"';
        metaData.style = 'white-space: normal;';
        return value;

    },
    createWindow: function() {
        //Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));

        Ext.QuickTips.init();
        Ext.apply(Ext.tip.QuickTipManager.getQuickTip(), {
            maxWidth: 800,
            minWidth: 100,
            showDelay: 50 // Show 50ms after entering target
        });
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('JavaEnvironmentBrowser-win');
        if (!win) {
        	var treestore = Ext.create('Ext.data.TreeStore', {
        		   model: 'urlsmpdel',
        		   proxy: {
        		      type: 'ajax',
        		      url: 'services/urlstree',
        		      reader: {
        		            type: 'json'
        		        }
        		   },
        		   //remoteFilter:false
        		   //folderSort: false,
        		   //autoLoad: true
        		});
        	  var tree = Ext.create('Ext.tree.Panel', {
        		   title: 'Classloader Hierarchy',
        		   width:200,
        		   split: true,
        		   collapsible: false,
        		   autoScroll: true,
                   border: 0,
        		   region: 'center',
        		   store: treestore,
        		   rootVisible: false,
				   viewConfig: {  //this config is passed to the view
				        loadMask: true
				    },
				   
        		   columns: [{
        		        xtype: 'treecolumn', //this is so we know which column will show the tree
        		        text: 'Forum',
        		        flex: 2.5,
        		        sortable: true,
        		        dataIndex: 'text',
        		        renderer: this.qtip
        		    },{
        		        text: 'Filename',
        		        flex: 1,
        		        dataIndex: 'filename',
        		        sortable: true,
        		        renderer: function(value, metaData,record, rowIdx,colIdx,store){
        		        	
        		            value = Ext.String.htmlEncode(value);
        		            if (record.get('duplicated')) {
        		            	metaData.tdAttr='data-qtip="<xmp>'+Ext.String.htmlEncode(value)+'</xmp>"'+ ' style="background-color:736F6E; border: 1px solid yellow;"';
        		            }else{
        		            	metaData.tdAttr='data-qtip="<xmp>'+Ext.String.htmlEncode(value)+'</xmp>"';
        		            }
        		            
        		            metaData.style = 'white-space: normal;';
        		            return value;

        		        }
        		    }]
        		   
        		});
        	  //treestore.load();
        	/*JDBCLogAnalyzerStore = Ext.create('Ext.data.Store', {
    			//pageSize: 50,
    			//remoteSort: true,
    	        model: 'analysis',
    	        data : [],//[{"count":1,"percentcount":2,"totaltime":3,"percenttime":4,"average":5,"standarddeviation":6,"query":"You successfully uploaded"}],
    	        proxy: {
    	            type: 'memory',
    	            reader: {
    	                type: 'json',
    	               //root: 'result'
    	            }
    	        }
    		});
        	 function renderItems(value, metaData,record, rowIdx,colIdx,store){
	            	value = Ext.String.htmlEncode(value);
	            	metaData.tdAttr='data-qtip="<xmp>'+Ext.String.htmlEncode(value)+'</xmp>"';
	            	return value;
	            };
	            function renderPercent(value, metaData,record, rowIdx,colIdx,store){
	            	value = Ext.String.htmlEncode(value);
	            	metaData.tdAttr='data-qtip="<xmp>'+Ext.String.htmlEncode(value)+'</xmp>"';
	            	return value+"%";
	            }; 
	            var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
	                clicksToEdit: 1
	            });
        	var grid = Ext.create('Ext.grid.Panel', {
        		//columnLines: true,
    	        height: '100%',
    	        width: '100%',
    	        id:'javaEnvironmentBrowserresult',
                store: JDBCLogAnalyzerStore,
                flex: 3,
                viewConfig: {
                    stripeRows: true
                    
                },
                plugins: [cellEditing],
                selModel: {
                    selType: 'cellmodel'
                },
                listeners : {
                    itemdblclick: function(dv, record, item, index, e) {
                        //alert(record.get('exceptions'));
                    	var query = record.get('query');
                    	var ex = Ext.decode(record.get('exceptions'))
                        if(exceptionsswindow ==null){
                        	function renderItems(value, metaData,record, rowIdx,colIdx,store){
            	            	value = Ext.String.htmlEncode(value);
            	            	metaData.tdAttr='data-qtip="<xmp>'+Ext.String.htmlEncode(value)+'</xmp>"';
            	            	return value;
            	            };
            	            var cellEditing2 = Ext.create('Ext.grid.plugin.CellEditing', {
            	                clicksToEdit: 1
            	            });
            	            
                            var s = Ext.create('Ext.data.Store', {
                    			//pageSize: 50,
                    			//remoteSort: true,
                    	        model: 'ExceptionModel',
                    	        data : ex,
                    	        proxy: {
                    	            type: 'memory',
                    	            reader: {
                    	                type: 'json',
                    	                
                    	            }
                    	        }
                    		});
                            
                            var exceptionsgp = Ext.create('Ext.grid.Panel', {
                    	        id:'javaEnvironmentBrowserexceptions',
                            	store: s,
                    	        columnLines: true,
                    	        plugins: [cellEditing2],
                                selModel: {
                                    selType: 'cellmodel'
                                },
                    	        columns: [{
                                    text: "timestamp",
                                    dataIndex: 'timestamp',
                                    flex:30,
                                    sortable: true,
                                    renderer: renderItems
                                },{
                                    text: "time",
                                    dataIndex: 'time',
                                    flex: 10,
                                    sortable: true,
                                    renderer: renderItems
                                },{
                                    text: "details",
                                    dataIndex: 'details',
                                    flex: 60,
                                    sortable: true,
                                    editor: {
                                        xtype: 'textfield'
                                    },
                                    renderer: renderItems
                                    
                                }],
                    	        height: '100%',
                    	        width: '100%',
                    	        viewConfig: {
                    	            stripeRows: true
                    	        }
                    		});
                        	exceptionsswindow = Ext.create('Ext.Window', {
                    	        title: query,
                    	        width: 800,
                    	        height: 300,
                    	        modal:true,
                    	        closeAction:'hide',
                    	        maximizable: true,
                    	        //plain: true,
                    	        //headerPosition: 'left',
                    	        layout: 'fit',
                    	        
                    	        items: [
                    	                exceptionsgp
                    	        ]
                    	        
                    	    });
                        	exceptionsswindow.setPosition(e.getXY());
                        	exceptionsswindow.show();
                    		
                    	}else{
                    		exceptionsswindow.setTitle(query);
                    		//alert(pkstring);
                    		//exceptionsswindow.removeAll();
                    		//exceptionsswindow.add(exceptionsgp);
                    		exceptionsswindow.down('#javaEnvironmentBrowserexceptions').getStore().loadData(ex,false);
                    		exceptionsswindow.setPosition(e.getXY());
                    		exceptionsswindow.doLayout();
                    		exceptionsswindow.show();
                    	}
                    }
                },
                // grid columns
                columns:[{
                    text: "count",
                    dataIndex: 'count',
                    flex:8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "percent count",
                    dataIndex: 'percentcount',
                    flex: 8,
                    sortable: true,
                    renderer: renderPercent
                },{
                    text: "totaltime",
                    dataIndex: 'totaltime',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "percent time",
                    dataIndex: 'percenttime',
                    flex: 8,
                    sortable: true,
                    renderer: renderPercent
                },{
                    text: "min",
                    dataIndex: 'min',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "max",
                    dataIndex: 'max',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "average",
                    dataIndex: 'average',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "standard deviation",
                    dataIndex: 'standarddeviation',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "query",
                    dataIndex: 'query',
                    flex: 36,
                    sortable: true,
                    editor: {
                        xtype: 'textfield'
                    },
                    renderer: renderItems
                }]
            });*/
        	 var formPanel = Ext.create('Ext.form.Panel', {
                 frame: true,
                 //flex: 1,
                 //title: 'Form Fields',
                 autoScroll: false,
                 bodyPadding: '0 0 0 0',
                 anchor: '100%',
                 //height:200,
                 layout: 'vbox',
                 fieldDefaults: {
                     labelAlign: 'top',
                     msgTarget: 'side'
                     
                     
                 },

                 items: [{
                     xtype: 'textfield',
                     name: 'name',
                     itemId: 'name',
                     fieldLabel: 'Search Resource (Regex)',
                     width: '100%'
                 }/*,{
                     xtype: 'textareafield',
                     grow: true,
                     name: 'result',
                     itemId: 'result',
                     readOnly:true,
                     height:63,
                     //fieldLabel: 'ImpexQuery',
                     //anchor: '100%, 100%',
                     autoScroll: true,
                     width: '100%'
                 }*/],
                 
                 buttons: [{
                     text: 'Clear',
                     handler: function(){
                    	 formPanel.down('#name').setValue('');
                    	 tree.getStore().proxy.url = 'services/urlstree';
                    	 tree.getStore().load();
                     }
                 },{
                     text: 'Search',
                     handler: function(){
                         if(Boolean(formPanel.down('#name').getValue())){
                        	 var url2 = 'services/findresource/?name='+encodeURIComponent(formPanel.down('#name').getValue());
                        	 
                        	 console.log(url2);
                        	 /*Ext.Ajax.request({ 
                     		    url:url2, 
                     		    failure: function(response, options) {
                     			   Ext.Msg.alert('Error', response.responseText);
                     		   	}, 
                     		    success: function(response, options) { 
                     		        console.log(response.responseText);
                     		       formPanel.down('#result').setValue(response.responseText);
                     		    } 
                     		});*/
                        	 tree.getStore().proxy.url = url2;
                        	 tree.getStore().load();
                         }
                     }
                 }]
             });
        	 var np = Ext.create('Ext.panel.Panel', {
                 region: 'north',
                 collapsible: true,
                 id: 'JavaEnvironmentBrowser-north',
                 layout: 'fit',
                 autoScroll: true,
                 border: 0,
                 items: [formPanel],
                 //title: "<img border='0' src='images/favicon.ico' height='16' width='16' />Attributes",
                 split: true,
                 //width: '60%',
                 //minWidth: 100,
                 //itemId: 'cp',
                 height:130,
                 minHeight: 130,
                 //bodyPadding: 10,
                 //stateId: 'centerRegion',
                 //stateful: true,
                 //html: 'center'
             });
            var cp = Ext.create('Ext.panel.Panel', {
                region: 'center',
                collapsible: false,
                id: 'JavaEnvironmentBrowser-center',
                layout: 'anchor',
                autoScroll: true,
                border: 0,
                items: [],
                //title: "<img border='0' src='images/favicon.ico' height='16' width='16' />Attributes",
                split: true,
                width: '60%',
                minWidth: 100,
                itemId: 'cp',
                minHeight: 160,
                //bodyPadding: 10,
                stateId: 'centerRegion',
                stateful: true,
                //html: 'center'
            });

            var ep = Ext.create('Ext.panel.Panel', {
                region: 'east',
                id: 'JavaEnvironmentBrowser-east',
                collapsible: true,
                collapsed: true,
                layout: 'fit',
                border: 0,
                title: 'ImpexQuery',
                items: [],
                split: true,
                width: '40%',
                minWidth: 100,
                itemId: 'ep',
                minHeight: 160,
                //bodyPadding: 10,
                stateId: 'eastRegion',
                stateful: true,
                html: 'east'
            });
            win = desktop.createWindow({
                id: 'JavaEnvironmentBrowser-win',
                title: 'JavaEnvironmentBrowser',
                width: 940,
                height: 480,
                iconCls: 'icon-java',
                animCollapse: true,
                constrainHeader: false,
                layout: {
                    type: 'border',
                    padding: 0
                },
                defaults: {
                    split: true
                },
                items: [
                    tree,np//, ep
                ],
                tools:[
    	        {
    	        	type:'help',
    	        	tooltip: 'Readme',
    	        	handler: function(event, toolEl, panel){
    	        		function showResult(btn){
    	        			
    	        	    };
    	        		Ext.MessageBox.show({
    	                    title: 'Any problems?',
    	                    msg: 'please contact tao.jiang02@sap.com',
    	                    buttons: Ext.MessageBox.YES,
    	                    buttonText:{ 
    	                        yes: "Got it!", 
    	                    },
    	                    fn: showResult
    	                });
    	        		
    	        		
    	        	}
    	        }],
                
            });
        }
        return win;
    }
});