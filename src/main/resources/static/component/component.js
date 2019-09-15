    var url = "/get-item";
    var store= Ext.create('Ext.data.Store',{
            fields : ["id","uuid","itemTitle","create_at"],
            proxy:{
                type : "ajax",
                url:url,
                reader:{
                    type:"json",
                    root:"data"
                },
            },
            autoLoad:true,
        });
        store.load();
    var todo_item_store_ed= Ext.create('Ext.data.Store',{
        fields : ["id","uuid","itemTitle","create_at"],
        proxy:{
            type : "ajax",
            url:url,
            reader:{
                type:"json",
                root:"data"
            },
        },
        autoLoad:true,
    });
    todo_item_store_ed.load()
    var todo_item_store= Ext.create('Ext.data.Store',{
        fields : ["id","uuid","itemTitle","create_at"],
        proxy:{
            type : "ajax",
            url:url,
            reader:{
                type:"json",
                root:"data"
            },
        },
        autoLoad:true,
    });
    todo_item_store.load()
     var treeStore = Ext.create("Ext.data.TreeStore",{
         root: {
             expanded: true,
             children: [
                 {text: "首页", expanded : true, children: [
                         {text:"首页",leaf : true,id:"select"},
                     ] },
                 { text: "笔记与事项", expanded: false, children: [
                         {text:"笔记",leaf : true},
                         {text:"事项",leaf : true}
                     ] },
                 { text: "管理", expanded: false, children: [
                         { text: "已删除记录", leaf: true },
                         { text: "已删除事项", leaf: true}
                     ] },
             ]
         },
     });
    Ext.define('Ext.tree.Panel.Left', {
        extend : "Ext.tree.Panel",
        alias : "widget.treePanel",
        title: "概览树",
        store: treeStore,
        rootVisible: false,
        useArrows : true,// 是否使用箭头样式
        autoScroll : true,// 滚动条
        animate : true,// 展开,收缩动画
        lines : false,// 禁止显示树的虚线
        listeners:{'rowclick':clickTreePanel,
            afterrender:function(){
                var record = this.getStore().getNodeById('select');
                this.getSelectionModel().select(record)
            }},
    });
    Ext.define('Item.Note', {
            extend : "Ext.Panel",
            bodyPadding: 1,
            title: '工具栏',
            alias : "widget.item_note",
            layout : "anchor",
            items : [
                {
                    xtype:"panel",
                    border : false,
                    style : {
                        marginTop : "3px"
                    },
                    items : [
                        {
                            xtype : "button",
                            text : "添加记录",
                            style :{
                                marginRight: '10px',
                                float:"right",
                            },
                            handler:function(){
                              var value = Ext.getCmp("item").getValue();
                              if(value===null||value===""){
                                  Ext.Msg.alert('提示', '内容不可为空');
                                  return
                              }
                                Ext.Ajax.request({
                                    url: '/add-item',
                                    params: {
                                        item: value
                                    },
                                    success: function(response){
                                        Ext.Msg.alert('成功', '添加事项成功！');
                                        url = "/get-item";
                                        store.getProxy().url = url;
                                        store.load()
                                    }
                                });

                            }
                        },
                        {
                            xtype : "textfield",
                            id : "item",
                            name : "item",
                            allowBlank: false,
                            style: {
                                width: '20%',
                                marginRight: '5px',
                                float:"right"
                            }
                        },
                        {
                            xtype : "button",
                            text : "确定时间",
                            style :{
                                marginRight: '25px',
                                float:"right",
                            },
                            handler:function () {
                                var value = Ext.util.Format.date(Ext.getCmp("selectionDate").getValue(),"Y-m-d")
                                if(value===null||value===""){
                                    Ext.Msg.alert('提示', '内容不可为空');
                                    return
                                }
                                url='/get-app-item?date='+value;
                                store.getProxy().url = url;
                                store.load()
                                Ext.getCmp("selectionDate").reset()
                            }
                        },
                        {
                            xtype: 'datefield',
                            id: "selectionDate",
                            name: 'from_date',
                            maxValue: new Date(),
                            style :{
                                marginRight: '10px',
                                float:"right",
                            },
                        },
                        {
                            xtype : "button",
                            id : "allItem",
                            text : "查看全部",
                            width : 100,
                            style :{
                                marginRight: '10px',
                                float:"right",
                            },
                            handler : function () {
                                url='/get-all-item'
                                store.getProxy().url = url;
                                store.load()
                            }
                        },
                        {
                            xtype : "textfield",
                            id : "search_item",
                            allowBlank: false,
                            style: {
                                width: '15%',
                                marginLeft: '5px',
                                float:"left",
                            }
                        },
                        {
                            xtype : "button",
                            text : "搜索",
                            width : 100,
                            style :{
                                marginLeft: '10px',
                                float:"left",
                            },
                            handler : function () {
                                var value = Ext.getCmp("search_item").getValue();
                                if(value===null||value===""){
                                    Ext.Msg.alert('提示', '内容不可为空');
                                    return
                                }
                                url="/search_item?text="+value
                                store.getProxy().url = url;
                                store.load({
                                    callback : function (r,options,success) {
                                        if(r.length===0){
                                            Ext.Msg.alert("提示","没有根据模糊查询查找到对应记录")
                                        }
                                    }
                                })
                            }
                        }
                    ]
                },
            ]
        });
        Ext.define("item.note.grid",{
            extend: "Ext.Panel",
            layout : "anchor",
            bodyPadding: 1,
            title : "记录",
            alias : "widget.item_grid",
            items : {
                xtype: "grid",
                id: "displayList",
                store: store,
                columns: [
                    { header: '序号',width : 45,
                        renderer: function (value, metaData, record,index) {
                            return Ext.String.format(index+1);
                        }},
                    {header: "index", dataIndex: "id", hidden: true,flex: 1},
                    {header: "id", dataIndex: "uuid", hidden: true,flex: 1},
                    {header: '标题', dataIndex: 'item_title', flex: 1},
                    {header: '创建时间', dataIndex: 'create_at', flex: 1},
                    {
                        header: "操作", minWidth: 200,flex: 1,
                        renderer: function (value, metaData, record) {
                            var uuid = record.data.uuid
                            var index = record.data.id
                            var button = "<button id = " + index + " data='" + uuid + "' type='button' class = 'button' onclick='addRecord(" + index + ")'>记录/查看笔记</button>" +
                                "<button id = " + index + " data='" + uuid + "' type='button' class = 'delete_button' onclick='deleteItem(" + index + ")'>删除</button>"+
                                "<button id = " + index + " data='" + uuid + "' type='button' class = 'down_button' onclick='downItem(" + index + ")'>下载HTML文件</button>"
                            return Ext.String.format(button);
                        }
                    },
                ],
            }

        });

    Ext.define("item.todo_item.grid",{
        extend: "Ext.Panel",
        bodyPadding: 1,
        title : "未完成",
        alias : "widget.todo_item_grid",
        items : {
            xtype: "grid",
            id: "display_todo_item_grid",
            store: todo_item_store,
            columns: [
                { header: '序号',width : 45,
                    renderer: function (value, metaData, record,index) {
                        return Ext.String.format(index+1);
                    }},
                {header: "index", dataIndex: "id", hidden: true,flex: 1},
                {header: "id", dataIndex: "uuid", hidden: true,flex: 1},
                {header: '标题', dataIndex: 'item_title', flex: 1},
                {header: '创建时间', dataIndex: 'create_at', flex: 1},
                {
                    header: "操作", minWidth: 250,flex: 1,
                    renderer: function (value, metaData, record) {
                        var uuid = record.data.uuid
                        var index = record.data.id
                        var button = "<button id = " + index + " data='" + uuid + "' type='button' class = 'button' onclick='addRecord(" + index + ")'>记录</button>" +
                            "<button id = " + index + " data='" + uuid + "' type='button' class = 'delete_button' onclick='deleteItem(" + index + ")'>删除</button>"+
                            "<button id = " + index + " data='" + uuid + "' type='button' class = 'button'>右移</button>"
                        return Ext.String.format(button);
                    }
                },
            ],
        }
    });


    Ext.define("item.todo_item_ed.grid",{
        extend: "Ext.Panel",
        layout : "anchor",
        bodyPadding: 1,
        title : "已完成",
        alias : "widget.todo_item_grid_ed",
        items : {
            xtype: "grid",
            id: "display_todo_item_grid_ed",
            store: todo_item_store_ed,
            columns: [
                { header: '序号',width : 45,
                    renderer: function (value, metaData, record,index) {
                        return Ext.String.format(index+1);
                    }},
                {header: "index", dataIndex: "id", hidden: true,flex: 1},
                {header: "id", dataIndex: "uuid", hidden: true,flex: 1},
                {header: '标题', dataIndex: 'item_title', flex: 1},
                {header: '创建时间', dataIndex: 'create_at', flex: 1},
                {
                    header: "操作", minWidth: 250,flex: 1,
                    renderer: function (value, metaData, record) {
                        var uuid = record.data.uuid
                        var index = record.data.id
                        var button = "<button id = " + index + " data='" + uuid + "' type='button' class = 'button' onclick='addRecord(" + index + ")'>记录</button>" +
                            "<button id = " + index + " data='" + uuid + "' type='button' class = 'delete_button' onclick='deleteItem(" + index + ")'>删除</button>"+
                            "<button id = " + index + " data='" + uuid + "' type='button' class = 'button'>左移</button>"
                        return Ext.String.format(button);
                    }
                },
            ],
        }
    });

    Ext.define('Item.ToDo.Item', {
        extend: "Ext.Panel",
        bodyPadding: 1,
        alias: "widget.todo_item",
        layout: "anchor",
        items : [
            {
                xtype:"panel",
                border : false,
                style : {
                    marginTop : "3px"
                },
                items : [
                    {
                        xtype : "button",
                        text : "添加事项",
                        style :{
                            marginRight: '10px',
                            float:"right",
                        },
                        handler:function(){
                            var value = Ext.getCmp("item").getValue();
                            if(value===null||value===""){
                                Ext.Msg.alert('提示', '内容不可为空');
                                return
                            }
                            Ext.Ajax.request({
                                url: '/add-item',
                                params: {
                                    item: value
                                },
                                success: function(response){
                                    Ext.Msg.alert('成功', '添加事项成功！');
                                    url = "/get-item";
                                    store.getProxy().url = url;
                                    store.load()
                                }
                            });

                        }
                    },
                    {
                        xtype : "textfield",
                        id : "todo_item",
                        name : "item",
                        allowBlank: false,
                        style: {
                            width: '20%',
                            marginRight: '5px',
                            float:"right"
                        }
                    },
                    {
                        xtype : "button",
                        text : "确定时间",
                        style :{
                            marginRight: '25px',
                            float:"right",
                        },
                        handler:function () {
                            var value = Ext.util.Format.date(Ext.getCmp("selectionDate").getValue(),"Y-m-d")
                            if(value===null||value===""){
                                Ext.Msg.alert('提示', '内容不可为空');
                                return
                            }
                            url='/get-app-item?date='+value;
                            store.getProxy().url = url;
                            store.load()
                            Ext.getCmp("selectionDate").reset()
                        }
                    },
                    {
                        xtype: 'datefield',
                        id: "todo_selectionDate",
                        name: 'from_date',
                        maxValue: new Date(),
                        style :{
                            marginRight: '10px',
                            float:"right",
                        },
                    },
                    {
                        xtype : "button",
                        id : "todo_allItem",
                        text : "查看全部",
                        width : 100,
                        style :{
                            marginRight: '10px',
                            float:"right",
                        },
                        handler : function () {
                            url='/get-all-item'
                            store.getProxy().url = url;
                            store.load()
                        }
                    },
                    {
                        xtype : "textfield",
                        id : "todo_search_item",
                        allowBlank: false,
                        style: {
                            width: '15%',
                            marginLeft: '5px',
                            float:"left",
                        }
                    },
                    {
                        xtype : "button",
                        text : "搜索",
                        width : 100,
                        style :{
                            marginLeft: '10px',
                            float:"left",
                        },
                        handler : function () {
                            var value = Ext.getCmp("search_item").getValue();
                            if(value===null||value===""){
                                Ext.Msg.alert('提示', '内容不可为空');
                                return
                            }
                            url="/search_item?text="+value
                            store.getProxy().url = url;
                            store.load({
                                callback : function (r,options,success) {
                                    if(r.length===0){
                                        Ext.Msg.alert("提示","没有根据模糊查询查找到对应记录")
                                    }
                                }
                            })
                        }
                    }
                ]
            },
        ]
    })

    function clickTreePanel(index,record,n){
        if (record.isLeaf()) {
            switch(record.raw.text) {
                case "首页":
                    Ext.getCmp("index").setHidden(false)
                    Ext.getCmp("todo_item_panel").setHidden(true);
                    Ext.getCmp("note_panel").setHidden(true);
                    break;
                case "笔记":
                    Ext.getCmp("index").setHidden(true)
                    Ext.getCmp("todo_item_panel").setHidden(true);
                    Ext.getCmp("note_panel").setHidden(false);
                    break;
                case "事项":
                    Ext.getCmp("index").setHidden(true)
                    Ext.getCmp("note_panel").setHidden(true);
                    Ext.getCmp("todo_item_panel").setHidden(false);
                    break;
                case "已删除记录":
                    console.log("已删除记录");
                    break;
                case "已删除事项":
                    console.log("已删除事项");
                    break;
            }
        }
    }
    function addRecord (value){
       var uuid = Ext.getElementById(value).getAttribute("data")
       var textData = "请输入内容......"
        Ext.Ajax.request({
            url: '/get-note-by-parentId?parent_id='+uuid,
            method : "GET",
            success: function(response){
                var json =JSON.parse(response.responseText)
                if(json.message!==""||json.message!==undefined){
                    textData = json.message
                    Ext.create('Ext.window.Window', {
                        title: '添加笔记',
                        height: 700,
                        width : 1000,
                        resizable:false,
                        layout: 'absolute',
                        bodyStyle:'background:#fff',
                        items:[
                            {
                                xtype : "htmleditor",
                                id : "value",
                                defaultValue : textData,
                                enableAlignments: true,
                                enableColors: true,
                                enableFont: true,
                                enableFontSize: true,
                                enableLinks: true,
                                enableFormat: true,
                                enableLists: true,
                                enableSourceEdit: true,
                                height : 626,
                            },
                            {
                                xtype : "button",
                                text : "保 存",
                                x : 893,
                                y : 630,
                                width : "85px",
                                height : "35px",
                                handler: function() {
                                    Ext.Ajax.request({
                                        url: '/add-note',
                                        params: {
                                            parentId : uuid,
                                            content : Ext.getCmp("value").getValue()
                                        },
                                        success: function(response){
                                            Ext.Msg.alert('成功', '添加笔记成功！');
                                        }
                                    });

                                }
                            }
                        ]
                    }).show();
                }
            }
        });
    }
    function deleteItem (value) {
        var uuid = Ext.getElementById(value).getAttribute("data")
        Ext.Msg.confirm("警告", "确定要删除吗？", function (button) {
            if (button == "yes") {
                Ext.Ajax.request({
                    url: '/delete_item',
                    params: {
                        uuid: uuid
                    },
                    success: function (response) {
                        Ext.Msg.alert('成功', '删除成功！');
                        url = '/get-item'
                        store.getProxy().url = url;
                        store.load()
                    }
                });
            }
        });
    }
    function downItem(value) {
        var uuid = Ext.getElementById(value).getAttribute("data")
        window.location.href="/down_file?uuid="+uuid;
    }
