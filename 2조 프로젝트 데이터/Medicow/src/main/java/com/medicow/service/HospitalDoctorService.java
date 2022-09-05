package com.medicow.service;

import com.medicow.model.constant.ActiveStatus;
import com.medicow.model.dto.DoctorFormDto;
import com.medicow.model.entity.Doctor;
import com.medicow.model.entity.Hospital;
import com.medicow.repository.inter.HospitalDoctorRepository;
import com.medicow.repository.inter.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HospitalDoctorService {
    private final HospitalDoctorRepository hospitalDoctorRepository;
    private final HospitalRepository hospitalRepository;
    // 의사 명단 조회하기
    public List<Doctor> searchMyDoctors(String hosId) {
//        Long hosNo = hospitalRepository.findHosNoByHosId(hosId);

        Hospital hospital = hospitalRepository.findByHosId(hosId);
        List<Doctor> doctorList = hospitalDoctorRepository.findByHosId(hospital);
        System.out.println("닥터서비스에서 문제가 있나봐 이리와봐");
//        List<DoctorFormDto> doctorDtoList = new ArrayList<DoctorFormDto>();
//
//        for (Doctor one : doctorList) {
//            doctorDtoList.add(one);
//        }
        return doctorList;
    }

    public Long maxNo(Hospital hosId) { // doctor의 아이디 중 가장 큰 값을 찾기
        System.out.println("여긴 오긴 오니?" );
        Long findNo = hospitalDoctorRepository.findByMaxNo(hosId);
        System.out.println("찾으러 갔다옴 맥스넘");
        if (findNo == null ) {
            findNo = Long.valueOf(1);
        }else{
            findNo+=1;
        }
        System.out.println(findNo+" 새로운 의사번호");
        return (findNo);
    }

    private final FileService fileService;
    @Value("${doctorImgLocation}")
    private String doctorImgLocation;
    public void addDoctor(Doctor doctor, MultipartFile doctorImgFile, String hosId)throws Exception { // doctor 정보 저장
        String oriImgName = doctorImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl="";

        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(doctorImgLocation, oriImgName, doctorImgFile.getBytes());
            imgUrl = "/images/doctor/"+imgName;
            System.out.println("서비스, 이미지 문제생김");
        }
        doctor.updateDoctorImg(oriImgName, imgName, imgUrl);

        Hospital findHosId = findHosId(hosId);
        System.out.println("hosId 찾아옴");
        System.out.println(findHosId.getHosId());

        Long docId = maxNo(findHosId);
        System.out.println(docId+ "의사 순번 정해졌는가!");
        doctor.setHospital(findHosId);
        doctor.setDocId(docId);
        doctor.setActiveStatus(ActiveStatus.ABLE);
        System.out.println(doctor.toString()+"-----------------------------------------------------------");

        hospitalDoctorRepository.save(doctor);
    }

    private Hospital findHosId(String hosId) {
        return hospitalRepository.findByHosId(hosId);
    }

    public DoctorFormDto findDoctor(Long docId) {
        Doctor doctor = hospitalDoctorRepository.findByDocId(docId);

        DoctorFormDto doctorFormDto = new DoctorFormDto();
        doctorFormDto.setHospital(doctor.getHospital());
        doctorFormDto.setDocId(doctor.getDocId());
        doctorFormDto.setDocName(doctor.getDocName());
        doctorFormDto.setHistory(doctor.getHistory());
        doctorFormDto.setDocImgUrl(doctor.getDocImgUrl());
        doctorFormDto.setDocOriImgName(doctor.getDocOriImgName());
        doctorFormDto.setDocImgName(doctor.getDocImgName());
        String[] subject = doctor.getDocSubject().split(" ");
        doctorFormDto.setDocSubject(subject);
        for (String item : subject) {
            System.out.println(item);
        }
        System.out.println("sdfsdfsd    "+ doctor.getDocSubject());

        return doctorFormDto;
    }

    public void updateDoctor(DoctorFormDto doctorFormDto,  MultipartFile doctorImgFile) throws Exception{
        Doctor doctor = hospitalDoctorRepository.findByDocId(doctorFormDto.getDocId());
        doctor.updateDoctor(doctorFormDto); // 화면에서 넘어온 dto 를 이용하여 새롭게 정보 수정

        if (!doctorImgFile.isEmpty()) {// 업로드할 파일이 있으면
            Doctor savedDoctorImg = hospitalDoctorRepository.findByDocId(doctorFormDto.getDocId());

            if(!StringUtils.isEmpty(savedDoctorImg.getDocImgName())){
                fileService.deleteFile(doctorImgLocation+"/"+savedDoctorImg.getDocImgName());
            }
            String oriImgName = doctorImgFile.getOriginalFilename(); // 이미지 원본 이름
            String imgName = fileService.uploadFile(doctorImgLocation, oriImgName,doctorImgFile.getBytes());
            String imgUrl = "/images/doctor/"+imgName;

            savedDoctorImg.updateDoctorImg(oriImgName, imgName, imgUrl);
        }

    }

}
