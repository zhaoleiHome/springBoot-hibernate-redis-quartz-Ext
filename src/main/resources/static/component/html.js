var HTMLEditor = Ext.extend(Ext.form.HtmlEditor, {
    addImage : function() {
        var editor = this;
        var imgform = new Ext.FormPanel({
            region : 'center',
            labelWidth : 60,
            frame : true,
            bodyStyle : 'padding:10px 5px 0',
            autoScroll : true,
            border : false,
            fileUpload : true,
            items : [{
                xtype : 'textfield',
                fieldLabel : '选择文件',
                id : 'accessoryUpload',
                name : 'accessoryUpload',
                inputType : 'file',
                allowBlank : false,
                blankText : '文件不能为空',
                anchor : '90%'
            },
                {
                    xtype:'fieldset',
                    title: '上传须知',
                    layout: {
                        type: 'table',
                        columns: 1
                    },
                    collapsible: false,//是否可折叠
                    defaultType : 'label',//默认的Form表单组件
                    items : [{
                        html: '1.上传图片不超过100KB'
                    },{
                        html : '2.为了保证图片能正常使用，我们支持以下格式的图片上传'
                    },{
                        html : '&nbsp;&nbsp;&nbsp;jpg,jpeg,png,gif'
                    }]
                }




            ],


            buttons : [{
                text : '上传',
                handler : function() {
                    if (!imgform.form.isValid()) {return;}
                    imgform.form.submit({
                        waitMsg : '正在上传......',
                        url : 'pictureUploadAction!upload.action',
                        success : function(form, action) {
                            var element = document.createElement("img");
                            element.src = 'upfiles/pic_uploadFiles/'+action.result.accessoryName;
                            element.width='500';
                            element.height='600';
//alert("element.src======"+action.result.accessoryName);
                            if (Ext.isIE) {
                                editor.insertAtCursor(element.outerHTML);

                            } else {
                                alert("===================");
                                var selection = editor.win.getSelection();
                                if (!selection.isCollapsed) {
                                    selection.deleteFromDocument();
                                }
                                selection.getRangeAt(0).insertNode(element);
                            }
                            //win.hide();//原始方法，但只能传一个图片
                            //更新后的方法
                            form.reset();
                            win.close();
                        },
                        failure : function(form, action) {
                            form.reset();
                            if (action.failureType == Ext.form.Action.SERVER_INVALID)
                                Ext.MessageBox.alert('警告','上传失败',action.result.errors.msg);
                        }
                    });
                }
            }, {
                text : '关闭',
                handler : function() {
                    win.close(this);
                }
            }]
        })

        var win = new Ext.Window({
            title : "上传图片",
            width : 400,
            height : 300,
            modal : true,
            border : false,
            iconCls : "ext/resources/images/default/picture_add.png",
            layout : "fit",
            items : imgform
        });
        win.show(this);
    },
    createToolbar : function(editor) {
        HTMLEditor.superclass.createToolbar.call(this, editor);
        this.tb.insertButton(16, {
            cls : "x-html-editor-tip",
            icon : "ext/resources/images/default/picture_add.png",
            handler : this.addImage,
            scope : this,
            //title :"插入图片",
            text :"插入图片"
        });
    }
});
Ext.reg('StarHtmleditor', HTMLEditor);
