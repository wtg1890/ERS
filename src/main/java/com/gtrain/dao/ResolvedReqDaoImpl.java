package com.gtrain.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.gtrain.model.Employee;
import com.gtrain.model.ResolvedReq;
import com.gtrain.model.ResolvedReq.RESOLVED;
import com.gtrain.util.ConnectionUtility;

public class ResolvedReqDaoImpl implements ResolvedReqDao {

	private static Logger logger = Logger.getLogger(ResolvedReqDaoImpl.class);
	
	private static ResolvedReqDaoImpl resolvedReqDaoImpl;
	
	private ResolvedReqDaoImpl() {}
	
	public static ResolvedReqDaoImpl getInstance() {
		if (resolvedReqDaoImpl == null) {
			resolvedReqDaoImpl = new ResolvedReqDaoImpl();
		}
		return resolvedReqDaoImpl;
	}
	
	
	
	@Override
	public boolean insert(ResolvedReq resolved) {

		try (Connection conn = ConnectionUtility.getConnection()) {
			int index = 0;
			
			String query = "insert into resolved_request values (?, ?, ?, ?, ?, ?)";
			String resManFullname = resolved.getResolvingManagerFirstname() + " " + resolved.getResolvingManagerLastname();
			PreparedStatement p = conn.prepareStatement(query);
			
			p.setString(++index, null);
			p.setInt(++index, resolved.getPendingRequestId());
			p.setInt(++index, resolved.getResolvingManagerId());
			p.setString(++index, resManFullname);
			p.setString(++index, resolved.getResolvedStatus());
			p.setString(++index, null);
			
			if (p.executeUpdate() > 0) {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.debug(c);
		}
		
		
		return false;
	}

	@Override
	public boolean insertProcedure(ResolvedReq resolved) {

		try (Connection conn = ConnectionUtility.getConnection()) {
			int index = 0;
			
			String query = "{CALL insert_resolved_request(?, ?, ?)}";
			
			CallableStatement c = conn.prepareCall(query);
			
			c.setInt(++index, resolved.getPendingRequestId());
			c.setInt(++index, resolved.getResolvingManagerId());
			c.setString(++index, resolved.getResolvedStatus());
			

			if (c.executeUpdate() > 0) {
				return true;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.debug(c);
		}
		
		
		return false;
	}

	@Override
	public ResolvedReq select(ResolvedReq resolved) {

		try (Connection conn = ConnectionUtility.getConnection()) {
			
			int index = 0;
			
			String query = "select * from resolved_request where res_req_id = ?";
			
			PreparedStatement p = conn.prepareStatement(query);
			
			p.setInt(++index, resolved.getResolvedRequestId());
			
			ResultSet rs = p.executeQuery();
			
			while (rs.next()) {
				return new ResolvedReq(
						rs.getInt(RESOLVED.ID.ordinal()),
						rs.getInt(RESOLVED.PENDID.ordinal()),
						rs.getInt(RESOLVED.MANAGERID.ordinal()),
						rs.getString(RESOLVED.MANAGERNAME.ordinal()),
						rs.getString(RESOLVED.STATUS.ordinal()),
						rs.getString(RESOLVED.RESOLVED_AT.ordinal()));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.debug(c);
		}
		return null;
	}
	
	
	public List<ResolvedReq> selectAllResolvedRequests() {
		List<ResolvedReq> list = new ArrayList<ResolvedReq>();
		
		try (Connection conn = ConnectionUtility.getConnection()) {
			
			String sql = "select * from resolved_request";
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				list.add(new ResolvedReq(
						rs.getInt(RESOLVED.ID.ordinal()),
						rs.getInt(RESOLVED.PENDID.ordinal()),
						rs.getInt(RESOLVED.MANAGERID.ordinal()),
						rs.getString(RESOLVED.MANAGERNAME.ordinal()),
						rs.getString(RESOLVED.STATUS.ordinal()),
						rs.getString(RESOLVED.RESOLVED_AT.ordinal())));
			}
			return list;
		} catch (SQLException e) {
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			logger.debug(c);
		}
		
		
		return null;
	}
	
	@Override
	public List<ResolvedReq> selectAll() {

		try (Connection conn = ConnectionUtility.getConnection()) {
			
			String query = "select r.res_req_id, r.pend_req_id, r.res_m_id, r.res_m_name, r.res_status, r.resolved_at, p.req_reason, p.req_amount, p.created_at"
					+ " from pending_request p inner join resolved_request r on p.pend_req_id = r.pend_req_id order by r.res_req_id";
			
			PreparedStatement p = conn.prepareStatement(query);
			
			ResultSet rs = p.executeQuery();
			
			List<ResolvedReq> resolvedRequests = new ArrayList<ResolvedReq>();
			
			while (rs.next()) {
				resolvedRequests.add(new ResolvedReq(
						rs.getInt(RESOLVED.ID.ordinal()),
						rs.getInt(RESOLVED.PENDID.ordinal()),
						rs.getInt(RESOLVED.MANAGERID.ordinal()),
						rs.getString(RESOLVED.MANAGERNAME.ordinal()),
						rs.getString(RESOLVED.STATUS.ordinal()),
						rs.getString(RESOLVED.RESOLVED_AT.ordinal()),
						rs.getString(RESOLVED.REQUEST_REASON.ordinal()),
						rs.getString(RESOLVED.REQUEST_AMOUNT.ordinal()),
						rs.getString(RESOLVED.CREATED_AT.ordinal())));
			}
			return resolvedRequests;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.debug(c);
		}
		return null;
	}

	@Override
	public List<ResolvedReq> selectAllByEmployee(Employee employee) {

		try (Connection conn = ConnectionUtility.getConnection()) {
			int index = 0;
			
			String query = "select r.res_req_id, r.pend_req_id, r.res_m_id, r.res_m_name, r.res_status, r.resolved_at, p.req_reason, p.req_amount, p.created_at"
					+ " from pending_request p inner join resolved_request r on p.pend_req_id = r.pend_req_id "
					+ "where p.e_id = ?";
			
			PreparedStatement p = conn.prepareStatement(query);
			
			p.setInt(++index, employee.getId());
			
			ResultSet rs = p.executeQuery();
			
			List<ResolvedReq> resolvedReqByEmployee = new ArrayList<ResolvedReq>();
			
			while (rs.next()) {
				resolvedReqByEmployee.add(new ResolvedReq(
						rs.getInt(RESOLVED.ID.ordinal()),
						rs.getInt(RESOLVED.PENDID.ordinal()),
						rs.getInt(RESOLVED.MANAGERID.ordinal()),
						rs.getString(RESOLVED.MANAGERNAME.ordinal()),
						rs.getString(RESOLVED.STATUS.ordinal()),
						rs.getString(RESOLVED.RESOLVED_AT.ordinal()),
						rs.getString(RESOLVED.REQUEST_REASON.ordinal()),
						rs.getString(RESOLVED.REQUEST_AMOUNT.ordinal()),
						rs.getString(RESOLVED.CREATED_AT.ordinal())));
			}
			return resolvedReqByEmployee;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.error(c.getMessage());
		}
		return null;
	}

}
