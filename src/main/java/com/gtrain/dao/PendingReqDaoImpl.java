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
import com.gtrain.model.PendingReq;
import com.gtrain.model.PendingReq.PENDING;
import com.gtrain.util.ConnectionUtility;

public class PendingReqDaoImpl implements PendingReqDao {
	
	private static Logger logger = Logger.getLogger(PendingReqDaoImpl.class);
	
	private static PendingReqDaoImpl pendingReqDaoImplementation;
	
	private PendingReqDaoImpl() {}
	
	public static PendingReqDaoImpl getInstance() {
		if (pendingReqDaoImplementation == null) {
			pendingReqDaoImplementation = new PendingReqDaoImpl();
		}
		return pendingReqDaoImplementation;
	}
	
	
	

	@Override
	public boolean insert(PendingReq pending) {

		try (Connection conn = ConnectionUtility.getConnection()) {
		
			int index = 0;
			
			String query = "insert into pending_request values (?, ?, ?, ?, ?)";
			
			PreparedStatement p = conn.prepareStatement(query);
			
			p.setString(++index, null); 	// Null because trigger will autoincrement id
			p.setInt(++index, pending.getRequestingEmployeeId());
			p.setString(++index, pending.getRequestAmount());
			p.setString(++index, pending.getRequestReason());
			p.setString(++index, null); 	// Null because trigger will automatically store and format date
											// that the pending request will make
			
			if (p.executeUpdate() > 0) {
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.debug(c);
		}
		return false;
	}

	@Override
	public boolean insertProcedure(PendingReq pending) {
		
		try (Connection conn = ConnectionUtility.getConnection()) {
			
			int index = 0;
			
			String query = "{CALL insert_pending_request(?, ?, ?)}";
			
			CallableStatement p = conn.prepareCall(query);
			
			p.setInt(++index, pending.getRequestingEmployeeId());
			p.setString(++index, pending.getRequestAmount());
			p.setString(++index, pending.getRequestReason());
			
			
			if (p.executeUpdate() > 0) {
				return true;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.debug(c);
		}
		
		return false;
	}

	@Override
	public PendingReq select(PendingReq pending) {
		
		try (Connection conn = ConnectionUtility.getConnection()) {
			int index = 0;
			
			String query = "select * from pending_request where pend_req_id = ?";
			
			PreparedStatement p = conn.prepareStatement(query);
			
			p.setInt(++index, pending.getRequestId());
			
			ResultSet rs = p.executeQuery();
			
			while (rs.next()) {
				return new PendingReq(
						rs.getInt(PENDING.ID.ordinal()),
						rs.getInt(PENDING.EMPID.ordinal()),
						rs.getString(PENDING.AMOUNT.ordinal()),
						rs.getString(PENDING.REASON.ordinal()),
						rs.getString(PENDING.CREATED_AT.ordinal())
						);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.debug(c);
		}
		
		return new PendingReq();
	}
	

	
	public List<PendingReq> selectAllPendingRequests() {
		List<PendingReq> list = new ArrayList<PendingReq>();
		
		try (Connection conn = ConnectionUtility.getConnection()) {
			
			String sql = "select * from pending_request";
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				list.add(new PendingReq(
						rs.getInt(PENDING.ID.ordinal()),
						rs.getInt(PENDING.EMPID.ordinal()),
						rs.getString(PENDING.AMOUNT.ordinal()),
						rs.getString(PENDING.REASON.ordinal()),
						rs.getString(PENDING.CREATED_AT.ordinal())
						));
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
	public List<PendingReq> selectAll() {
		try (Connection conn = ConnectionUtility.getConnection()) {
			String query = "select p.* from pending_request p left join resolved_request r on p.pend_req_id = r.pend_req_id where r.res_req_id is null";
			
			PreparedStatement p = conn.prepareStatement(query);
			
			ResultSet rs = p.executeQuery();
			
			List<PendingReq> pendingRequests = new ArrayList<PendingReq>();
			
			while (rs.next()) {
				pendingRequests.add(new PendingReq(
						rs.getInt(PENDING.ID.ordinal()),
						rs.getInt(PENDING.EMPID.ordinal()),
						rs.getString(PENDING.AMOUNT.ordinal()),
						rs.getString(PENDING.REASON.ordinal()),
						rs.getString(PENDING.CREATED_AT.ordinal())
						));
			}
			return pendingRequests;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.debug(c);
		}
		
		return new ArrayList<>();
	}
	
	@Override
	public List<PendingReq> selectAllByEmployee(Employee employee) {
		
		try (Connection conn = ConnectionUtility.getConnection()) {
			int index = 0;
			
			String query = "select p.* from pending_request p left join resolved_request r on p.pend_req_id = r.pend_req_id where r.res_req_id is null and p.e_id = ?";
			
			PreparedStatement p = conn.prepareStatement(query);
			
			p.setInt(++index, employee.getId());
			
			ResultSet rs = p.executeQuery();
			
			List<PendingReq> pendingRequests = new ArrayList<PendingReq>();
			while (rs.next()) {
				pendingRequests.add(new PendingReq(
						rs.getInt(PENDING.ID.ordinal()),
						rs.getInt(PENDING.EMPID.ordinal()),
						rs.getString(PENDING.AMOUNT.ordinal()),
						rs.getString(PENDING.REASON.ordinal()),
						rs.getString(PENDING.CREATED_AT.ordinal())
						));
			}
			return pendingRequests;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e);
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			logger.debug(c);
		}
		
		return new ArrayList<>();
	}
	
	

}
