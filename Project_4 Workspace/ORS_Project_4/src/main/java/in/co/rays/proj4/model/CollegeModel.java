package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * JDBC Implementation of CollegeModel
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
public class CollegeModel {

	private static Logger log = Logger.getLogger(CollegeModel.class);

	/**
	 * Find next PK of College
	 * 
	 * @throws DatabaseException
	 */
	public Integer nextPK() throws DatabaseException {

		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID)FROM ST_COLLEGE");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...e");
			// throw new DatabaseException("Exception:Exception in getting PK");
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End ");
		return pk + 1;

	}

	// --------------------***********************-----------------------************--------------------//
	/**
	 * Add a College
	 * 
	 * @param bean
	 * @throws DatabaseException
	 */
	public long add(CollegeBean bean) throws ApplicationException, DuplicateRecordException {

		log.debug("Model add Started");
		Connection conn = null;
		int pk = 0;

		// CollegeBean duplicateCollegeName = findByName(bean.getName());

		/*
		 * if(duplicateCollegeName!=null) { throw new
		 * DuplicateRecordException("College Name already exists "); }
		 */
		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_COLLEGE VALUES(?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getAddress());
			pstmt.setString(4, bean.getState());
			pstmt.setString(5, bean.getCity());
			pstmt.setString(6, bean.getPhoneNo());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("Database Exception...e");

			try {
				conn.rollback();
			} catch (Exception ex) {
				// ex.printStackTrace();
				throw new ApplicationException("Exception: add rollback exception" + ex.getMessage());

			}
			throw new ApplicationException("Exception: Exception in add College");
			// e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;

	}

	// --------------------***********************-----------------------************--------------------//
	/**
	 * Delete a College
	 * 
	 * @param bean
	 * @throws DatabaseException
	 */

	public void delete(CollegeBean bean) throws ApplicationException {

		log.debug("Model Delete Started");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COLLEGE WHERE ID = ?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("Database execution.....e");
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception: delete rollback exception" + ex.getMessage());

			}
			throw new ApplicationException("exception : exception in delete college");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Delete End");
	}

	// --------------------***********************-----------------------************--------------------//
	/**
	 * Update a College
	 * 
	 * @param bean
	 * @throws DuplicateRecordException
	 * @throws DatabaseException
	 */
	public void update(CollegeBean bean) throws ApplicationException, DuplicateRecordException {

		log.debug("Model Update Started");
		Connection conn = null;
		CollegeBean beanExist = findByName(bean.getName());

		// Check if updated College already exist

		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("College is already exist");
		}

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			// Begin Transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE ST_COLLEGE SET NAME=?,ADDRESS=?,STATE=?,CITY=?,PHONE_NO=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getAddress());
			pstmt.setString(3, bean.getState());
			pstmt.setString(4, bean.getCity());
			pstmt.setString(5, bean.getPhoneNo());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.setLong(10, bean.getId());

			pstmt.executeUpdate();
			conn.commit();
			conn.close();
			pstmt.close();
		} catch (Exception e) {
			log.error("Databse Exception...e");

			try {
				conn.rollback();

			} catch (Exception ex) {
				e.printStackTrace();
				// throw new ApplicationException("Exception : Delete rollback
				// exception"+ex.getMessage());
			}
			// throw new ApplicationException("Exception in updating College");
			e.printStackTrace();

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Update End");

	}

	// --------------------***********************-----------------------************--------------------//
	/**
	 * Find User by College
	 * 
	 * @param login : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */
	public CollegeBean findByName(String name) throws ApplicationException {

		log.debug("Model FindByName Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE NAME =?");

		CollegeBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
			}
			rs.close();

		} catch (Exception e) {
			log.error("database Exception e..... ");
			// throw new ApplicationException("Exception:Exception in getting College by
			// Name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByName End");
		return bean;
	}

	// --------------------***********************-----------------------************--------------------//
	/**
	 * Find User by College
	 * 
	 * @param pk : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public CollegeBean findByPK(long pk) {
		log.debug("Model findByPK started");
		// StringBuffer sql = new StringBuffer("SELECT FROM ST_COLLEGE WHERE ID =?");
		CollegeBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_college where id=?");
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// System.out.println(bean.getName());
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception......e");
			e.printStackTrace();
			// throw new ApplicationException("Exception : Exception in geting College by
			// pk");

		} finally {
			JDBCDataSource.closeConnection(conn);
			return bean;

		}
	}

	// --------------------***********************-----------------------************--------------------//
	/**
	     * Search College with pagination
	     * @return list : List of Users
	     * @param bean
	     * : Search Parameters
	     * @param pageNo
	     * : Current Page No.
	     * @param pageSize
	     * : Size of Page
	     * @throws DatabaseException
	     */
		public List search(CollegeBean bean,int pageNo,int pageSize)throws ApplicationException{
			
			log.debug("Model Search started");
			StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE WHERE 1=1");
			if(bean!=null) {
				
				if(bean.getId()>0) {
					sql.append("AND id = "+bean.getId());
				}
				if(bean.getName()!=null && bean.getName().length()>0){
					sql.append("AND NAME like"+bean.getName()+"%");
				}
				if(bean.getAddress()!=null && bean.getAddress().length()>0) {	
					sql.append("AND ADDRESS like"+bean.getAddress()+"%");
				}
				if(bean.getState()!=null && bean.getState().length()>0) {
					sql.append("AND STATE like"+bean.getState()+"%");
				}
				if(bean.getCity()!=null && bean.getCity().length()>0) {
					sql.append("AND CITY like"+bean.getCity()+"%");
				}
				if(bean.getPhoneNo()!=null && bean.getPhoneNo().length()>0) {
					sql.append("AND PHONE_NO like"+bean.getPhoneNo()+"%");
				}
			}
			 // if page size is greater than zero then apply pagination
		
		
		  if(pageSize>0) { 
			  // Calculate start record index
			  pageNo =(pageNo-1)*pageSize; 
		      sql.append(" limit "+pageNo+","+pageSize);
		  }
		 
		 
					ArrayList list = new ArrayList();
					Connection conn = null;
					try {
						conn = JDBCDataSource.getConnection();
						PreparedStatement pstmt = conn.prepareStatement(sql.toString());
						ResultSet rs = pstmt.executeQuery();
						while(rs.next()) {
							bean = new CollegeBean();
							bean.setId(rs.getLong(1));
							bean.setName(rs.getString(2));
			                bean.setAddress(rs.getString(3));
			                bean.setState(rs.getString(4));
			                bean.setCity(rs.getString(5));
			                bean.setPhoneNo(rs.getString(6));
			                bean.setCreatedBy(rs.getString(7));
			                bean.setModifiedBy(rs.getString(8));
			                bean.setCreatedDatetime(rs.getTimestamp(9));
			                bean.setModifiedDatetime(rs.getTimestamp(10));
			                list.add(bean);
			       }
						rs.close();
			}catch (Exception e) {
                   log.error("Datebase Exception..e");
                   e.printStackTrace();
                //  throw new ApplicationException("Exception : Exception in search college");
                   
			}finally {
				JDBCDataSource.closeConnection(conn);
			}
					log.debug("Model search End");
				return list;
		}

	// --------------------***********************-----------------------************--------------------//
	/**
	 * Search College
	 *
	 * @param bean : Search Parameters
	 * @throws DatabaseException
	 */
	public List search(CollegeBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Get List of College
	 *
	 * @return list : List of College
	 * @throws DatabaseException
	 */
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of College with pagination
	 *
	 * @return list : List of College
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws DatabaseException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COLLEGE");
		// if page size is greater than zero then apply pagination

		if (pageSize > 0) { // Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append("limit" + pageNo + "," + pageSize);
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CollegeBean bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...e");
			e.printStackTrace();
			// throw new ApplicationException
			// ("Exception: Exception in getting list of users");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

}
