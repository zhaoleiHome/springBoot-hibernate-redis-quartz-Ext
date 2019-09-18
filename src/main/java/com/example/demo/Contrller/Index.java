package com.example.demo.Contrller;
import com.example.demo.DataBase.Items;
import com.example.demo.DataBase.Notes;
import com.example.demo.DataBase.ToDoItem;
import com.example.demo.DataBase.im.AddItem;
import com.example.demo.DataBase.im.AddNote;
import com.example.demo.DataBase.im.AddToDoItem;
import com.example.demo.JsonResult;
import com.example.demo.RedisCache.imp.RedisService.RedisCache;
import io.netty.util.concurrent.SucceededFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.MetaIndex;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RequestWrapper;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class Index {
    private static Integer SUCCESS=20;
    private static Integer ERROR = 30;

    @Autowired
    private AddItem addItem;
    @Autowired
    private AddNote addNote;
    @Autowired
    private AddToDoItem addToDoItem;
    @Autowired
    private RedisCache redisCache;

    @RequestMapping(value = "add-item",method = RequestMethod.POST)
    public JsonResult<Void> addItem(String item){
        Items items  = new Items();
        items.setItem_title(item);
        items.setCreate_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String uuid = UUID.randomUUID().toString();
        items.setUuid(uuid);
        items.setDelete_state(0);
        addItem.save(items);
        redisCache.deleteCache(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return new JsonResult<Void>(SUCCESS);
    }

    @RequestMapping(value = "add-note",method =RequestMethod.POST)
    public JsonResult<Void> addNode(String parentId,String content){
        Notes notes = new Notes();
        notes.setContent(content);
        notes.setParent_id(parentId);
        notes.setUuid(UUID.randomUUID().toString());
        notes.setCreate_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Integer count = addNote.findCount(parentId);
        if(count!=0){
            addNote.updateNotes(content,parentId);
        }else{
            addNote.save(notes);
        }
        return new JsonResult<Void>(SUCCESS);
    }

    @RequestMapping(value = "get-note-by-parentId",method = RequestMethod.GET)
    public JsonResult<String > getNoteByItemId(String parent_id){
        Notes notes = addNote.findByParent_id(parent_id);
        if(notes==null){
            notes = new Notes();
            notes.setContent("请输入内容......");
        }
        return new JsonResult(SUCCESS,notes.getContent());
    }

    //自行获取今日笔记的接口
    @RequestMapping(value = "get-item",method = RequestMethod.GET)
    public JsonResult<List> getAllItem(String date){
        Long time1 = System.currentTimeMillis();
        List list = redisCache.getAllItemByDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//        List list = addItem.findByAppDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Long time2 = System.currentTimeMillis();
        long time = (int)(time2-time1);
        System.err.println("执行时间为"+time+"毫秒");
        return new JsonResult(SUCCESS,list);
    }
    @RequestMapping(value = "get-app-item",method = RequestMethod.GET)
    public JsonResult<List> getAppDateItem(String date){
        List list = addItem.findByAppDate(date);
        return new JsonResult<List>(SUCCESS,list);
    }
    @RequestMapping(value = "get-all-item",method = RequestMethod.GET)
    public JsonResult<List> getAllItem() {
        List<Items> list = addItem.findAll();
        return new JsonResult<>(SUCCESS, list);
    }
    @RequestMapping(value = "delete_item",method = RequestMethod.POST)
    public JsonResult<Void> deleteItem(String uuid){
        addItem.deleteItem(uuid);
        redisCache.deleteCache(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return new JsonResult<>(SUCCESS);
    }

    @RequestMapping(value = "search_item",method = RequestMethod.GET)
    public JsonResult<List> searchLikeText(String text){
        List items = addItem.findLikeText(text);
        return new JsonResult<List>(SUCCESS,items);
    }
    @RequestMapping(value = "down_file",method = RequestMethod.GET)
    public JsonResult<List> createFile(HttpServletRequest request, HttpServletResponse response){
        List list = addNote.downLoadFileByHtml(request.getParameter("uuid"));
        Object[] obj = (Object[]) list.get(0);
        String title =  obj[0].toString();
        String content = obj[1].toString();
        String uuid = obj[2].toString();
        File dir = new File("D:\\downloadTemp");
            if(!dir.isDirectory()){ //文件夹存在
                dir.mkdir();  //创建文件夹
            }
            try{
                File file = new File(dir.getPath()+"\\"+title+"_"+uuid+".html");
                if(file.exists()){
                    file.delete();
                }
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(content);
                bw.close();
                File downFile = new File(file.getPath());
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
                byte[] buffer = new byte[2048];
                FileInputStream fileInputStream = new FileInputStream(downFile);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                OutputStream outputStream = response.getOutputStream();
                int i = bufferedInputStream.read(buffer);
                while (i!=-1){
                    outputStream.write(buffer,0,i);
                    i = bufferedInputStream.read(buffer);
                }
                fileInputStream.close();
                outputStream.close();
                bufferedInputStream.close();
                file.delete();
            }catch (Exception e){
                e.printStackTrace();
            }

        return null;
    }
    @RequestMapping(value = "add-todo-item",method = RequestMethod.POST)
   public JsonResult<Void> addToDoItem(String title,String date){
       ToDoItem toDoItem = new ToDoItem();
       toDoItem.setTitle(title);
       toDoItem.setUuid(UUID.randomUUID().toString());
       toDoItem.setDate(date);
       toDoItem.setCreate_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
       toDoItem.setState(0);
       toDoItem.setDelete_state(0);
       addToDoItem.save(toDoItem);
       return new JsonResult<Void>(SUCCESS);
   }
    @RequestMapping(value = "get-todo-item-date",method = RequestMethod.GET)
   public JsonResult<List<ToDoItem>> getToDoItem(){
        List<ToDoItem>list = addToDoItem.findAllByDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),0);
        return new JsonResult<List<ToDoItem>>(SUCCESS,list);
   }
    @RequestMapping(value = "get-todo-item-date-ED",method = RequestMethod.GET)
    public JsonResult<List<ToDoItem>> getToDoItemED(){
        List<ToDoItem>list = addToDoItem.findAllByDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),1);
        return new JsonResult<List<ToDoItem>>(SUCCESS,list);
    }
    @RequestMapping(value = "change-todo-item-state",method = RequestMethod.POST)
    public JsonResult<Void> changeToDoItemState(String uuid){
        ToDoItem toDoItem = addToDoItem.findById(uuid);
        if(!toDoItem.getDate().equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){
            return new JsonResult<Void>(ERROR);
        }
        Integer state = toDoItem.getState();
        switch (state){
            case 0:
                state=1;
                break;
            case 1:
                state=0;
                break;
        }
        addToDoItem.changeToDoItemState(uuid,state);
        return new JsonResult<Void>(SUCCESS);
    }

    @RequestMapping(value = "get-all-todo-item",method = RequestMethod.GET)
    public JsonResult<List<ToDoItem>> getAllToDoItem(String state_rd){
        List<ToDoItem> list = null;
        if(state_rd.equals("ed")){
           list = addToDoItem.findAllByState(1);
        }else{
           list = addToDoItem.findAllByState(0);
        }
        return new JsonResult<List<ToDoItem>>(SUCCESS,list);
    }
    @RequestMapping(value = "delete-todo-item",method = RequestMethod.GET)
    public JsonResult<Void> deleteToDoItem(String uuid){
        addToDoItem.deleteToDoItemByUUID(uuid);
        return new JsonResult<Void>(SUCCESS);
    }

    @RequestMapping(value = "get-todo-item-by-date",method = RequestMethod.GET)
    public JsonResult<List<ToDoItem>> getToDoItemByDate(String date,Integer state){
        return new JsonResult<List<ToDoItem>>(SUCCESS,addToDoItem.findAllByDate(date,state));
    }

    public JsonResult<List<ToDoItem>> SearchToDoItem(String text,Integer state){
        return new JsonResult<List<ToDoItem>>(SUCCESS,addToDoItem.findAllByDate(text,state));
    }
}













