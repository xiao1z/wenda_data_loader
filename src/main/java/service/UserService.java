package service;

import org.apache.ibatis.session.SqlSession;
import dao.MybatisSqlSessionFactory;
import dao.UserDAO;
import model.User;


public class UserService {
	
	public int addUser(User user){
		SqlSession session = MybatisSqlSessionFactory.getSqlSessionFactory().openSession();
		UserDAO userDAO;
		int result = -1;
		try{
			userDAO = session.getMapper(UserDAO.class);
			userDAO.addUser(user);
			session.commit();
			result = user.getId();
		}catch(Exception e)
		{
			e.printStackTrace();
			return -1;
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
