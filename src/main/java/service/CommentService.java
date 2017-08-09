package service;

import org.apache.ibatis.session.SqlSession;
import dao.CommentDAO;
import dao.MybatisSqlSessionFactory;
import model.Comment;

public class CommentService {
	
	
	public int addComment(Comment comment)
	{
		SqlSession session=MybatisSqlSessionFactory.getSqlSessionFactory().openSession();
		CommentDAO commentDAO;
		int result = -1;
		try{
			commentDAO = session.getMapper(CommentDAO.class);
			commentDAO.addComment(comment);
			session.commit();
			result = comment.getId();
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
