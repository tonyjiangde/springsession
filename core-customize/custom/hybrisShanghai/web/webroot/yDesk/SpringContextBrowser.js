Ext.require('Ext.chart.*');
Ext.require(['Ext.layout.container.Fit', 'Ext.window.MessageBox']);
Ext.require([
             'Ext.grid.*',
             'Ext.data.*',
             'Ext.util.*',
             'Ext.tip.QuickTipManager',
         ]); 

Ext.define('contextmodel', {
    extend: 'Ext.data.TreeModel',
    fields: [
      
        { name: 'text', type: 'string'},
        {name: 'parent', type:'string'}
      /*  { name: 'leaf', type: 'boolean', mapping: 'leaf' },
        { name: 'loaded', type: 'boolean', mapping: 'loaded', defaultValue: false },
        { name: 'expanded', defaultValue: true }*/
    ]
});

Ext.define('beaninfomodel', {
    extend: 'Ext.data.Model',
    fields: [
      
        { name: 'name', type: 'string'},
        {name: 'classname', type:'string'},
        {name: 'alias', type:'string'},
        {name: 'scope', type:'string'},
        {name: 'isSingleton', type:'boolean'},
        {name: 'isAbstract', type:'boolean'}
      /*  { name: 'leaf', type: 'boolean', mapping: 'leaf' },
        { name: 'loaded', type: 'boolean', mapping: 'loaded', defaultValue: false },
        { name: 'expanded', defaultValue: true }*/
    ]
});

Ext.define('beandetailsmodel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'propertyname', type: 'string'},
        {name: 'propertyclass', type:'string'},
        {name: 'propertydeclaringclass', type:'string'}
    ]
});
Ext.define('hybrisDesktop.SpringContextBrowser', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.data.ArrayStore',
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],
    
    
    beandetailswindow: null,
    
    id: 'SpringContextBrowser-win',
    searchmode: false,
    searchingdata: null,
    searchingmode: null,
    init: function() {
        this.launcher = {
            text: 'SpringContextBrowser',
            iconCls: 'icon-spring'
        };
    },
    qtip: function(value, metaData,record, rowIdx,colIdx,store){
        value = Ext.String.htmlEncode(value);
        //metaData.tdAttr='data-qtip="<xmp>'+Ext.String.htmlEncode(value)+'</xmp>"';
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
        var me = this;
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('SpringContextBrowser-win');
        if (!win) {
        	var treestore = Ext.create('Ext.data.TreeStore', {
        		   model: 'contextmodel',
        		   proxy: {
        		      type: 'ajax',
        		      url: 'services/springcontext',
        		      reader: {
        		            type: 'json'
        		        }
        		   },
        		   //remoteFilter:false
        		   //folderSort: false,
        		   //autoLoad: true
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
        	
        	var states = Ext.create('Ext.data.Store', {
        	    fields: ['mode'],
        	    data : [
        	        {"mode":"all"},
        	        {"mode":"name"},
        	        {"mode":"alias"},
        	        {"mode":"class"}
        	        //...
        	    ]
        	});

        	// Create the combo box, attached to the states data store
        	var combo =Ext.create('Ext.form.ComboBox', {
        	    fieldLabel: 'Choose field',
        	    store: states,
        	    itemId: 'combo',
        	    queryMode: 'local',
        	    width: '20%',
        	    editable: false,
        	    forceSelection: true,
        	    displayField: 'mode',
        	    valueField: 'mode'
 
        	});
        	combo.select(combo.getStore().getAt(0));
        	 var formPanel = Ext.create('Ext.form.Panel', {
                 frame: true,
                 //flex: 1,
                 //title: 'Form Fields',
                 autoScroll: false,
                 bodyPadding: '0 0 0 0',
                 anchor: '100%',
                 //height:200,
                 layout: 'hbox',
                 fieldDefaults: {
                     labelAlign: 'top',
                     msgTarget: 'side'
                     
                     
                 },

                 items: [{
                     xtype: 'textfield',
                     name: 'name',
                     itemId: 'name',
                     fieldLabel: 'Search Resource (Regex)',
                     width: '80%'
                 },
                 combo/*,{
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
                    	 tree.getStore().proxy.url = 'services/springcontext';
                    	 tree.getStore().load();
                    	 me.searchmode = false;
                    	 me.searchingmode = null;
                    	 me.searchingdata =null;
                    	 beangrid.getStore().removeAll();
                    	 beangrid.getStore().proxy.url = '';
                     }
                 },{
                     text: 'Search',
                     handler: function(){
                         if(Boolean(formPanel.down('#name').getValue())){
                        	 var input =encodeURIComponent(formPanel.down('#name').getValue());
                        	 var mode = formPanel.down('#combo').getValue();
                        	 var url2 = 'services/springcontext/search?';
                        	 if(mode=="all"){
                        		 url2 = url2+"beanid="+input+"&alias="+input+"&class="+input;
                        	 }else if(mode=="name"){
                        		 url2 = url2+"beanid="+input;
                        	 }else if(mode=="alias"){
                        		 url2 = url2+"alias="+input;
                        	 }else if(mode=="class"){
                        		 url2 = url2+"class="+input;
                        	 }
                        	 me.searchmode = true;
                        	 me.searchingmode = mode;
                        	 me.searchingdata =input;
                        	 tree.getStore().proxy.url = url2;
                        	 tree.getStore().load();
                        	 beangrid.getStore().removeAll();
                        	 beangrid.getStore().proxy.url = '';
                         }
                     }
                 }]
             });
        	 var np = Ext.create('Ext.panel.Panel', {
                 region: 'north',
                 collapsible: true,
                 id: 'SpringContextBrowser-north',
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
                id: 'SpringContextBrowser-center',
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
            var beanstore = Ext.create('Ext.data.Store', {
        		//id: 'beanstore',
                //pageSize: 50,
                //buffered: true,
                //leadingBufferZone: 150,
                model: 'beaninfomodel',
                proxy: {
                    type: 'ajax',
                    url: '',
                    reader: {
                        type: 'json'
                    }
                },
                autoLoad: false
            });
            function renderItems(value, metaData,record, rowIdx,colIdx,store){
            	value = Ext.String.htmlEncode(value);
            	metaData.tdAttr='data-qtip="<xmp>'+Ext.String.htmlEncode(value)+'</xmp>"';
            	return value;
            };
            
            var beangrid = Ext.create('Ext.ux.LiveSearchGridPanel', {
        		//columnLines: true,
    	        height: '100%',
    	        width: '100%',
    	        id:'springcontextbeangrid',
                store: beanstore,
                flex: 3,
                viewConfig: {
                    stripeRows: true,
					loadMask: true
                },
                selModel: {
                    selType: 'cellmodel'
                },
                listeners : {
                    itemdblclick: function(dv, record, item, index, e) {
                    }
                },
                // grid columns
                
                columns:[{
                    text: "Name",
                    dataIndex: 'name',
                    flex:8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "Classname",
                    dataIndex: 'classname',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "Alias",
                    dataIndex: 'alias',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "Scope",
                    dataIndex: 'scope',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "Singleton",
                    dataIndex: 'isSingleton',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                },{
                    text: "Abstract",
                    dataIndex: 'isAbstract',
                    flex: 8,
                    sortable: true,
                    renderer: renderItems
                }],
                
                listeners : {
    	        	itemclick: function(dv, record, item, index, e) {
    	            	var beanid = record.get('name');
    	            	console.log(beanid+"----"+ep.selection);
    	            	if(beanid && ep.selection){
    	            		Ext.Ajax.request({ 
    	            		    url:'services/springcontext/'+btoa(ep.selection)+'/'+btoa(beanid),
    	            		    failure: function(response, options) {
    	            		    	Ext.Msg.alert('Error', response.responseText);
    	            		    }, 
    	            		    success: function(response, options) { 
    	            		    	
    	            		    	var dataObj = Ext.decode(response.responseText); 
    	            		    	if(dataObj==null){
    	                       		 	Ext.Msg.alert('Error', 'No details are found!???');
    	            		    	}else{    
    	            		    		console.log(dataObj);
    	            		    		var s = Ext.create('Ext.data.Store', {
	                            			//pageSize: 50,
	                            			//remoteSort: true,
	                            	        model: 'beandetailsmodel',
	                            	        data : dataObj,
	                            	        proxy: {
	                            	            type: 'memory',
	                            	            reader: {
	                            	                type: 'json',
	                            	                root: 'properties',
	                            	            }
	                            	        }
	                            		});
    	            		    		
	                            		var sp = Ext.create('Ext.grid.Panel', {
	                            	        store: s,
	                            	        columnLines: true,
	                            	        columns: [{
	                                            text: "Propertyname",
	                                            dataIndex: 'propertyname',
	                                            flex:8,
	                                            sortable: true,
	                                            renderer: renderItems
	                                        },{
	                                            text: "Propertyclass",
	                                            dataIndex: 'propertyclass',
	                                            flex: 8,
	                                            sortable: true,
	                                            renderer: renderItems
	                                        },{
	                                            text: "Propertydeclaringclass",
	                                            dataIndex: 'propertydeclaringclass',
	                                            flex: 8,
	                                            sortable: true,
	                                            renderer: renderItems
	                                        }],
	                            	        height: '100%',
	                            	        width: '100%',
	                            	        viewConfig: {
	                            	            stripeRows: true
	                            	        }
	                            		});
    	            		    		if(me.beandetailswindow==null){    	            		    			
    	            		    			me.beandetailswindow = Ext.create('Ext.Window', {
	                                	        title: 'Bean '+beanid,
	                                	        width: 800,
	                                	        height: 500,
	                                	        modal:true,
	                                	        closeAction:'hide',
	                                	        maximizable: true,
	                                	        layout: 'fit',
	                                	        items: [
	                                	                sp
	                                	        ]
	                                	        
	                                	    });
    	            		    			me.beandetailswindow.setPosition(e.getXY());
    	            		    			me.beandetailswindow.show();
    	            		    		}else{
    	            		    			me.beandetailswindow.setTitle('Bean '+beanid);
	                                		//alert(pkstring);
    	            		    			me.beandetailswindow.removeAll();
    	            		    			me.beandetailswindow.add(sp);
    	            		    			me.beandetailswindow.setPosition(e.getXY());
    	            		    			me.beandetailswindow.doLayout();
    	            		    			me.beandetailswindow.show();
    	            		    		}
    	            		    	} 
    	            		    }
    	            		});	            	
    	            	}    	            	
    	        	}
                }
            });
            var ep = Ext.create('Ext.panel.Panel', {
                region: 'east',
                id: 'SpringContextBrowser-east',
                collapsible: true,
                collapsed: true,
                selection: '',
                layout: 'fit',
                border: 0,
                title: 'Beans',
                items: [beangrid],
                split: true,
                width: '40%',
                minWidth: 100,
                itemId: 'ep',
                //minHeight: 160,
                //bodyPadding: 10,
                stateId: 'springeastRegion',
                stateful: true
            });
            var tree = Ext.create('Ext.tree.Panel', {
     		   title: 'Spring Context Hierarchy',
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
     		        text: 'Context',
     		        flex: 2.5,
     		        sortable: true,
     		        dataIndex: 'text',
     		        renderer: this.qtip
     		    },{
     		        text: 'Parent',
     		        flex: 1,
     		        dataIndex: 'parent',
     		        sortable: true,
     		        renderer: this.qtip
     		    }],
     		    listeners: {
     		        itemclick: function(s,r) {
     		        	console.log(me.searchmode);
     		        	if(r.data.leaf==true){
     		        		if(!me.searchmode){
     		        			Ext.getCmp('SpringContextBrowser-east').expand();
         		        		console.log(ep.title);
         		        		ep.setTitle('Beans in '+r.data.text);
         		        		ep.selection = r.data.text;
         		        		beangrid.getStore().proxy.url = 'services/springcontext/'+btoa(r.data.text);
         		        		beangrid.getStore().load();
     		        		}else{
         		        		
         		        		Ext.getCmp('SpringContextBrowser-east').expand();
         		        		console.log(ep.title);
         		        		ep.setTitle('Beans in '+r.data.text);
         		        		ep.selection = r.data.text;
         		        		var base64 = btoa(r.data.text);
         		        		var url2 = 'services/springcontext/search/'+base64+'?';
         		        		
	                           	 if(me.searchingmode=="all"){
	                           		 url2 = url2+"beanid="+me.searchingdata+"&alias="+me.searchingdata+"&class="+me.searchingdata;
	                           	 }else if(me.searchingmode=="name"){
	                           		 url2 = url2+"beanid="+me.searchingdata;
	                           	 }else if(me.searchingmode=="alias"){
	                           		 url2 = url2+"alias="+me.searchingdata;
	                           	 }else if(me.searchingmode=="class"){
	                           		 url2 = url2+"class="+me.searchingdata;
	                           	 }
	                           	
         		        		beangrid.getStore().proxy.url = url2;
         		        		beangrid.getStore().load();
         		        	}  
     		        		
     		        	}    		                
     		        }
     		    }
     		   
     		});
            
            win = desktop.createWindow({
                id: 'SpringContextBrowser-win',
                title: 'SpringContextBrowser',
                width: 940,
                height: 480,
                iconCls: 'icon-spring',
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
                    tree,np,ep//, ep
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