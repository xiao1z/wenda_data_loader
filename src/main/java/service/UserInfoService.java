package service;

import org.apache.ibatis.session.SqlSession;
import dao.MybatisSqlSessionFactory;
import dao.UserInfoDAO;
import model.UserInfo;


public class UserInfoService {
	
	
	
	public int addUserInfo(UserInfo userInfo)
	{
		SqlSession session=MybatisSqlSessionFactory.getSqlSessionFactory().openSession();
		
		UserInfoDAO userInfoDAO;
		int result = -1;
		try{
			userInfoDAO = session.getMapper(UserInfoDAO.class);
			result = userInfoDAO.addUserInfo(userInfo);
			session.commit();
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			if(session!=null)
			{
				session.close();
			}
		}
		return result;
	}
	
	
	
}
