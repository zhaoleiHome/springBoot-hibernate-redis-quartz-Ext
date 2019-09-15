package com.example.demo.Contrller;
import com.example.demo.DataBase.Items;
import com.example.demo.DataBase.Notes;
import com.example.demo.DataBase.im.AddItem;
import com.example.demo.DataBase.im.AddNote;
import com.example.demo.DataBase.im.AddToDoItem;
import com.example.demo.JsonResult;
import com.example.demo.RedisCache.imp.RedisService.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        List list = addItem.findByAppDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
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
    @RequestMapping(value = "get_cache",method = RequestMethod.GET)
    public void getCache(){
        redisCache.getToDoItemById(1);
    }
}













