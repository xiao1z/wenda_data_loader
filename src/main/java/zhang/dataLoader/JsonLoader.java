package zhang.dataLoader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import model.Comment;
import model.Img;
import model.Question;
import model.User;
import model.UserInfo;

/**
 * Hello world!
 *
 */
public class JsonLoader 
{
	
	/*
	 * StringBuilder headUrl = new StringBuilder("/wenda/headImg/")
				.append(userId).append("/").append(headImg.getOriginalFilename());
		
		StringBuilder netUrl = new StringBuilder("/wenda/img/")
				.append(questionId).append("/").append(commentId).append("-").append(offset).append("-").append(commentImg.getOriginalFilename());
	 */
	private String sqlPath;
	private Map<Integer,Integer> idMap = new HashMap<Integer,Integer>();
	private Random random = new Random();
	private service.UserService userService = new service.UserService();
	private service.CommentService commentService = new service.CommentService();
	private service.ImgService imgService = new service.ImgService();
	private service.UserInfoService userInfoService = new service.UserInfoService();
	private service.QuestionService questionService = new service.QuestionService();
	private List<Entry<Integer,Integer>> userIdList;
	//头像图片根地址
	private static String NET_HEAD_URL;
	
	//其他图片根地址
	private static String NET_IMG_URL;
	
	public JsonLoader(String sqlPath,String netHeadUrl,String netImgUrl){
		NET_HEAD_URL = netHeadUrl;
		NET_IMG_URL = netImgUrl;
		this.sqlPath = sqlPath;
		loadUser();
		loadQuestion();
		loadComment();
		loadImg();
	}
	
	private int getRandomUserId(){
		int randomInt = Math.abs(random.nextInt()) % userIdList.size();
		return userIdList.get(randomInt).getValue();
	}
	
	private void loadUser()
	{
		String path = sqlPath + "/user";
		
		try {
			int cannotResolveCount = 0;
			int resloveCount = 0;
			BufferedReader in = new BufferedReader(new FileReader(path));
			String jsonString;
			while((jsonString = in.readLine())!=null)
			{
				jsonString = jsonString.substring(1, jsonString.length()-1).replace("\\\"", "\"");
				//System.out.println(jsonString);
				User user = null;
				try{
					user = JSONObject.parseObject(jsonString, User.class);
					user.setPassword("");
					user.setSalt("");
					user.setHeadUrl(NET_HEAD_URL+user.getHeadUrl());
					int oldId = user.getId();
					int result = userService.addUser(user);
					if(result!=-1)
						idMap.put(oldId,result);
					UserInfo userInfo = new UserInfo();
					userInfo.setUserId(result);
					userInfoService.addUserInfo(userInfo);
					
				}catch(JSONException e){
					cannotResolveCount++;
					//e.printStackTrace();
					continue;
				}
				if(user!=null)
					resloveCount++;
			}
		
			System.out.println("User:"+resloveCount+" cannot:"+cannotResolveCount);
			Set<Entry<Integer,Integer>> idSet = idMap.entrySet();
			userIdList = new ArrayList<Entry<Integer,Integer>>(idSet);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadQuestion()
	{
		String path = sqlPath + "/question";
		
		try {
			int cannotResolveCount = 0;
			BufferedReader in = new BufferedReader(new FileReader(path));
			int resloveCount = 0;
			String jsonString;
			
			while((jsonString = in.readLine())!=null)
			{
				jsonString = jsonString.substring(1, jsonString.length()-1).replace("\\\"", "\"");
				//System.out.println(jsonString);
				jsonString = jsonString.replace("\\\\n", "<br>");
				//System.out.println(jsonString);
				Question question = null;
				try{
					
					question = JSONObject.parseObject(jsonString, Question.class);
					question.setUserId(getRandomUserId());
					int oldId = question.getId();
					int result = questionService.addQuestion(question);
					if(result!=-1)
						idMap.put(oldId, result);
				}catch(JSONException e){
					cannotResolveCount++;
					continue;
				}
				if(question!=null)
					resloveCount++;
			}
		
			System.out.println("Question:"+resloveCount+" cannot"+cannotResolveCount);
			//System.out.println("idMap after load question:"+idMap);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadComment()
	{
		String path = sqlPath + "/comment";
		
		try {
			int cannotResolveCount = 0;
			BufferedReader in = new BufferedReader(new FileReader(path));
			int resloveCount = 0;
			String jsonString;
			while((jsonString = in.readLine())!=null)
			{
				//System.out.println(jsonString);
				jsonString = jsonString.substring(1, jsonString.length()-1).replace("\\\"", "\"");
				jsonString = jsonString.replace("\\\\n", "<br>");
				Comment comment = null;
				try{
					comment = JSONObject.parseObject(jsonString, Comment.class);
					if(idMap.get(comment.getEntityId())==null)
						continue;
					comment.setEntityId(idMap.get(comment.getEntityId()));
					if(idMap.get(comment.getUserId())==null)
						comment.setUserId(getRandomUserId());
					else
						comment.setUserId(idMap.get(comment.getUserId()));
					int oldId = comment.getId();
					int result = commentService.addComment(comment);
					if(result!= -1)
						idMap.put(oldId, result);
				}catch(JSONException e){
					cannotResolveCount++;
					//e.printStackTrace();
					continue;
				}
				if(comment!=null)
					resloveCount++;
			}
		
			System.out.println("Comment"+resloveCount+" cannot:"+cannotResolveCount);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadImg()
	{
		String path = sqlPath + "/img";
		
		try {
			int cannotResolveCount = 0;
			BufferedReader in = new BufferedReader(new FileReader(path));
			int resloveCount = 0;
			String jsonString;
			while((jsonString = in.readLine())!=null)
			{
				//System.out.println(jsonString);
				jsonString = jsonString.substring(1, jsonString.length()-1).replace("\\\"", "\"");
				//jsonString.replace("\\n", "<br>");
				Img img = null;
				try{
					img = JSONObject.parseObject(jsonString, Img.class);
					if(idMap.get(img.getEntityId())==null)
						continue;
					img.setEntityId(idMap.get(img.getEntityId()));
					img.setUrl(NET_IMG_URL+img.getUrl());
					imgService.addImg(img);
				}catch(JSONException e){
					cannotResolveCount++;
					//e.printStackTrace();
					continue;
				}
				if(img!=null)
					resloveCount++;
			}
		
			System.out.println("Img:"+resloveCount+" cannot:"+cannotResolveCount);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static void main( String[] args )
    {
    	String netHeadUrl = "/wenda/headImg";
		String netImgUrl = "/wenda/img";
        new JsonLoader("D:/wendaDataJson",netHeadUrl,netImgUrl);
    }
}
