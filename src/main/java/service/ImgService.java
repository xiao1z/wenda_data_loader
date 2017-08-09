package service;

import org.apache.ibatis.session.SqlSession;
import dao.ImgDAO;
import dao.MybatisSqlSessionFactory;
import model.Img;

public class ImgService {
	
	
	public int addImg(Img img)
	{
		SqlSession session = MybatisSqlSessionFactory.getSqlSessionFactory().openSession();
		ImgDAO imgDAO;
		int result = -1;
		try{
			imgDAO = session.getMapper(ImgDAO.class);
			result= imgDAO.addImg(img);
			session.commit();
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
