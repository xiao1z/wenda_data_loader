package service;

import org.apache.ibatis.session.SqlSession;
import dao.MybatisSqlSessionFactory;
import dao.QuestionDAO;
import model.Question;


public class QuestionService{
	
	public int addQuestion(Question question)
	{
		SqlSession session=MybatisSqlSessionFactory.getSqlSessionFactory().openSession();
		QuestionDAO questionDAO;
		int result = -1;
		try{
			questionDAO = session.getMapper(QuestionDAO.class);
			questionDAO.addQuestion(question);
			session.commit();
			result = question.getId();
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
