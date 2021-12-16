package com.base.dao;

import com.base.models.CustomerInfo;
import com.base.models.OrderDetails;
import com.base.models.Product;
import com.base.utils.DButil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//This class is used for interacting with database (Writing queries and getting information)
public class CustomerDAO {
    
    public boolean addCustomerToDB(CustomerInfo csi) {
        Connection conn = null;
        boolean flag = false;
        try {
            conn = DButil.getConnection("addCustomerToDB");
            String query = "INSERT INTO Customer_Info(First_Name,Last_Name,Mobile_Number,E_Mail_ID,Password) VALUES (?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, csi.getFirstName());
            pstmt.setString(2, csi.getLastName());
            pstmt.setString(3, csi.getPhoneNumber());
            pstmt.setString(4, csi.geteMailID());
            pstmt.setString(5, csi.getPassword());

            int n = pstmt.executeUpdate();
            if (n > 0) {
                flag = true;
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DButil.closeConnection(conn, "addCustomerToDB");
        }

        return flag;
    }

    
    
    public boolean addOrderDetailsToDB(CustomerInfo csi,Product product) {
        boolean flag = false;
        Connection conn=null;
        try {
            conn = DButil.getConnection("addOrderDetailsToDB");
            System.out.println("hello Before pstmt");
            String query = "INSERT INTO Product_Details (Customer_ID,Parcel_Type,Parcel_Weight_KG,EST_Distance,Order_Placed_Date,EST_Delivery_Date,Fee,Payment_Type) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            System.out.println("hello After pstmt");
            pstmt.setInt(1,csi.getCustomerID());
            pstmt.setString(2,product.getParcelType());
            pstmt.setDouble(3,product.getParcelWeightInKG());
            pstmt.setDouble(4,product.getDistance());
            pstmt.setString(5,product.getOrderDate());
            pstmt.setString(6,product.getEstDeliveryDate());
            pstmt.setDouble(7,product.getFee());
            pstmt.setString(8,product.getPaymentType());
            System.out.println("hello Before execute");
            pstmt.executeUpdate();
            flag=true;
        }catch (Exception exception){
            exception.printStackTrace();
        }finally {
            DButil.closeConnection(conn,"addOrderDetailsToDB");
        }
        return flag;
    }
    
    public Integer getOrderIDFromDB(CustomerInfo csi,Product product) {
        Connection conn = null;
        try {
            conn = DButil.getConnection("getOrderIDFromDB");
            String query = "SELECT Order_ID FROM Product_Details WHERE Customer_ID=? AND Parcel_Type=? AND Parcel_Weight_KG=? AND EST_Distance=? AND Order_Placed_Date=? AND EST_Delivery_Date=? AND Fee=? AND Payment_Type=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,csi.getCustomerID());
            pstmt.setString(2,product.getParcelType());
            pstmt.setDouble(3,product.getParcelWeightInKG());
            pstmt.setDouble(4,product.getDistance());
            pstmt.setString(5,product.getOrderDate());
            pstmt.setString(6,product.getEstDeliveryDate());
            pstmt.setDouble(7,product.getFee());
            pstmt.setString(8,product.getPaymentType());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                product.setOrderID(rs.getInt("Order_ID"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            DButil.closeConnection(conn,"getOrderIDFromDB");
        }
        return product.getOrderID();
    }

    public boolean addSenderDetailsToDB(CustomerInfo csi,OrderDetails sender) {
        Connection conn = null;
        boolean flag = false;
        try {
            conn = DButil.getConnection("addSenderDetailsToDB");
            String query = "INSERT INTO Sender_Details(Order_ID,Customer_ID,Name,Sender_Address,City,Pincode,Contact_Number) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,sender.getOrderId());
            pstmt.setInt(2,csi.getCustomerID());
            pstmt.setString(3,sender.getName());
            pstmt.setString(4,sender.getAddress());
            pstmt.setString(5,sender.getCity());
            pstmt.setInt(6,Integer.parseInt(sender.getPincode()));
            pstmt.setString(7,sender.getPhone());
            int n = pstmt.executeUpdate();
            if (n>0){
                flag=true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            DButil.closeConnection(conn,"addSenderDetailsToDB");
        }
        return flag;
    }
    
    public boolean addReceiverDetailsToDB(CustomerInfo csi,OrderDetails receiver) {
        Connection conn = null;
        boolean flag = false;
        try {
            conn = DButil.getConnection("addSenderDetailsToDB");
            String query = "INSERT INTO Recipient_Details(Order_ID,Customer_ID,Name,Pincode,City,Delivery_Address,Contact_Number) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,receiver.getOrderId());
            pstmt.setInt(2, csi.getCustomerID());
            pstmt.setString(3, receiver.getName());
            pstmt.setInt(4, Integer.parseInt(receiver.getPincode()));
            pstmt.setString(5, receiver.getCity());
            pstmt.setString(6, receiver.getAddress());
            pstmt.setString(7, receiver.getPhone());
            int n = pstmt.executeUpdate();
            if (n > 0) {
                flag = true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            DButil.closeConnection(conn,"addSenderDetailsToDB");
        }
        return flag;
    }

    // USED IN LOGIN TO CHECK PASSWORD
    public CustomerInfo findCustomerByEmailID(String emailID) {
        Connection conn = null;
        CustomerInfo csi = new CustomerInfo();
        try {
            conn = DButil.getConnection("findCustomerByEmailID");
            String query = "SELECT Customer_ID,E_Mail_ID,Password,First_Name,Last_Name,Mobile_Number FROM Customer_Info WHERE E_Mail_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, emailID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                csi.setCustomerID(rs.getInt("Customer_ID"));
                csi.seteMailID(rs.getString("E_Mail_ID"));
                csi.setPassword(rs.getString("Password"));
                csi.setFirstName(rs.getString("First_Name"));
                csi.setLastName(rs.getString("Last_Name"));
                csi.setPhoneNumber(rs.getString("Mobile_Number"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DButil.closeConnection(conn, "findCustomerByEmailID");
        }
        return csi;
    }

    public boolean checkIfMailAlreadyRegistered(String emailID) {
        Connection conn = null;
        boolean ifEmailExists = false;
        try {
            conn = DButil.getConnection("checkIfMailAlreadyRegistered");
            String query = "SELECT 1 FROM Customer_Info WHERE E_Mail_ID =?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, emailID);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ifEmailExists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DButil.closeConnection(conn, "checkIfMailAlreadyRegistered");
        }
        return ifEmailExists;
    }


    public Product getProductDetailsFromDB(CustomerInfo csi, Product product) {
        Connection conn = null;
        try {
            conn = DButil.getConnection("getProductDetailsFromDB");
            String query = "SELECT Order_ID, Parcel_Type, Parcel_Weight_KG, Order_Placed_Date, EST_Delivery_Date, Dimensions, Fee, Payment_Type FROM Product_Details WHERE Customer_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, csi.getCustomerID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                product.setOrderID(rs.getInt("Order_ID"));
                product.setParcelType(rs.getString("Parcel_Type"));
                product.setParcelWeightInKG(rs.getDouble("Parcel_Weight_KG"));
                product.setOrderDate(rs.getString("Order_Placed_Date"));
                product.setEstDeliveryDate(rs.getString("EST_Delivery_Date"));
                product.setFee(rs.getDouble("Fee"));
                product.setPaymentType(rs.getString("Payment_Type"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DButil.closeConnection(conn, "getProductDetailsFromDB");
        }
        return product;
    }

    public boolean updateCustomerDetails(CustomerInfo customerInfo) {
        boolean flag = false;
        Connection conn = null;
        try {
            conn = DButil.getConnection("updateCustomerDetails");
            String query = "UPDATE Customer_Info SET First_Name = ?, Last_Name = ?, Mobile_Number = ? , Password = ? WHERE E_Mail_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, customerInfo.getFirstName());
            pstmt.setString(2, customerInfo.getLastName());
            pstmt.setString(3, customerInfo.getPhoneNumber());
            pstmt.setString(4, customerInfo.getPassword());
            pstmt.setString(5, customerInfo.geteMailID());
            int i = pstmt.executeUpdate();
            if (i > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DButil.closeConnection(conn, "updateCustomerDetails");
        }
        return flag;
    }
}
