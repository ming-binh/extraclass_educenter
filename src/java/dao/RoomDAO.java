package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modal.RoomModal;
import utils.DBUtil;

/**
 *
 * @author Admin
 */
public class RoomDAO {

    public List<RoomModal> getAllRooms() {
        List<RoomModal> rooms = new ArrayList<>();

        String sql = "SELECT * FROM room ORDER BY id";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RoomModal room = new RoomModal();
                room.setId(rs.getString("id"));
                room.setRoomName(rs.getString("room_name"));
                rooms.add(room);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static void main(String[] args) {
        RoomDAO roomDao = new RoomDAO();
        List<RoomModal> rooms = roomDao.getAllRooms();

        for (RoomModal r : rooms) {
            System.out.println(r.getRoomName());
        }

    }
}
