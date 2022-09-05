package com.medicow.repository.inter;

import com.medicow.model.constant.DiagnosisStatus;
import com.medicow.model.constant.ReservationStatus;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// 예약을 위한 Repository
public interface HospitalReservationRepository extends JpaRepository<Reservation,Long> {
    // 해당 병원에 예약한 환자들을 검색하는 쿼리문(해당 병원의 hosNo와 pageable을 매개변수로 삼음)
    @Query(value=" select h from Hospital h where h.hosNo = :hosNo ")
    public Hospital findHospitalByhosNo(@Param("hosNo") Long hosNo);

    @Query(value=" select r from Reservation r join Hospital h on r.hospital = h.hosNo where h.hosNo = :hosNo order By r.regTime desc ")
    public List<Reservation> findReservationByHosNo(@Param("hosNo") Long hosNo, Pageable pageable);

    // 전체 예약 수를 확인하는 메소드
    @Query(value=" select count(r) from Reservation r join Hospital h on r.hospital = h.hosNo where h.hosNo = :hosNo ")
    Long countByHosNo(@Param("hosNo")Long hosNo);

    //입력한 이메일에 대한 멤버 아이디를 들고옴
    @Query(value = "select m.id from Member m where m.email = :email")
    public Long findMemberIdByEmail(@Param("email") String email);

    // 검색 조회시 환자 이메일을 기준으로 확인하는 메소드
    @Query(value =" select r from Reservation r join Member m on r.member = m.id where  m.id = :id and r.hospital = :hos order By r. regTime desc ")
    public List<Reservation> findReservationByEmail(@Param("id") Long id,@Param("hos") Hospital hospital, Pageable pageable);
    // 이메일 검색시 결과 총 수
    @Query(value= " select count(r) from Reservation r join Member m on r.member = m.id where  m.id = :id ")
    public Long countByEmail(@Param("id") Long id);

    // 환자 이름
    @Query(value = " select r from Reservation r join Member m on r.member = m.id where m.email in (select m.email from m where m.name like %:name%) and r.hospital = :hos order By r. regTime desc ")
    public List<Reservation> findReservationByName(@Param("name") String name,@Param("hos") Hospital hos, Pageable pageable );
    // 환자 이름 검색시 결과 총 수
    @Query(value= "  select count(r) from Reservation r join Member m on r.member = m.id where m.email in (select m.email from m where m.name like %:name%) and r.hospital = :hos ")
    public Long countByName(@Param("name") String name,@Param("hos") Hospital hos);

    // 승인여부
    @Query(value=" select r from Reservation r join Hospital h on r.hospital = h.hosNo where h.hosNo = :hosNo and r.reservationStatus = :reservationStatus order By r.regTime desc ")
    public List<Reservation> findReservationByReservationStatus(@Param("hosNo") Long hosNo,@Param("reservationStatus") ReservationStatus reservationStatus, Pageable pageable);

    @Query(value=" select count(r) from Reservation r join Hospital h on r.hospital = h.hosNo where h.hosNo = :hosNo and r.reservationStatus = :reservationStatus ")
    public Long countByReservationStatus(@Param("hosNo") Long hosNo,@Param("reservationStatus") ReservationStatus reservationStatus);

    // 증상으로 찾을 때
    @Query(value = " select r from Reservation r where r.hospital = :hos and r.symptom like %:symptom% ")
    public List<Reservation> findReservationBySymptom(@Param("hos") Hospital hospital, @Param("symptom") String symptom, Pageable pageable);

    // 증상으로 검색시 결과 총 수
    @Query(value= " select count(r) from Reservation r where r.hospital = :hos and r.symptom like %:symptom% ")
    public Long countBySymptom(@Param("hos") Hospital hospital, @Param("symptom") String symptom);

    // 진료 여부
    @Query(value=" select r from Reservation r join Hospital h on r.hospital = h.hosNo where h.hosNo = :hosNo and r.diagnosisStatus = :diagnosisStatus order By r.regTime desc ")
    public List<Reservation> findReservationByDiagnosisStatus(@Param("hosNo") Long hosNo, @Param("diagnosisStatus") DiagnosisStatus diagnosisStatus, Pageable pageable);
    // 진료 여부로 검색시 결과 총 수
    @Query(value=" select count(r) from Reservation r join Hospital h on r.hospital = h.hosNo where h.hosNo = :hosNo and r.diagnosisStatus = :diagnosisStatus ")
    public Long countByDiagnosisStatus(@Param("hosNo") Long hosNo, @Param("diagnosisStatus") DiagnosisStatus diagnosisStatus);

    // 예약상태 업데이트 하는 쿼리문
    @Modifying
    @Query(value= " update Reservation r set r.reservationStatus = :reservationStatus  where r.id = :id " )
    public Integer updateReservationStatus(@Param("reservationStatus") ReservationStatus reservationStatus, @Param("id") Long id);

    // 진료상태 업데이트 하는 쿼리문
    @Modifying
    @Query(value= " update Reservation r set r.diagnosisStatus = :diagnosisStatus  where r.id = :id " )
    public Integer updateDiagnosisStatus(@Param("diagnosisStatus") DiagnosisStatus diagnosisStatus, @Param("id") Long id);

    @Query(value=" select count(r) from Reservation r where r.hospital =:hospital and r.reservationStatus=:reservationStatus and r.diagnosisStatus=:diagnosisStatus")
    Long curReservationNo(@Param("hospital")Hospital hospital, ReservationStatus reservationStatus,DiagnosisStatus diagnosisStatus);

    @Query(value=" select count(r) from Reservation r where r.hospital =:hospital and r.reservationStatus= :reservationStatus and r.diagnosisStatus= :diagnosisStatus ")
    Long curWaitingNo(@Param("hospital")Hospital hospital,@Param("reservationStatus") ReservationStatus reservationStatus, @Param("diagnosisStatus") DiagnosisStatus diagnosisStatus);
}
