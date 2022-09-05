package com.medicow.service;

import com.medicow.model.constant.RegisterStatus;
import com.medicow.model.dto.*;
import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.HospitalImg;
import com.medicow.repository.inter.HospitalImgRepository;
import com.medicow.repository.inter.HospitalRepository;
import com.medicow.repository.inter.MemHospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HospitalService implements UserDetailsService {
    private final HospitalRepository hospitalRepository;
    private final HospitalImgRepository hospitalImgRepository;
    private final MemHospitalRepository memHospitalRepository;

    public Page<Hospital> getHospList(HospitalFindDto hospitalFindDto, Pageable pageable) {
        System.out.println("=============================== HospitalService");
        return memHospitalRepository.getHospList(hospitalFindDto, pageable);
    }

    // 병원 리스트 페이지에서 예약하기 버튼 클릭시 예약페이지로 이동.
    public HospitalDetailDto getHospDtl(Long hosNo) {

        // 병원 Entity 정보를 구합니다.
        Hospital hospital = memHospitalRepository.findById(hosNo)
                .orElseThrow(EntityNotFoundException::new);
        HospitalDetailDto hospitalDetailDto = HospitalDetailDto.of(hospital);

        System.out.println("===================== Hospitalservice");
        System.out.println(hospitalDetailDto.getHosName());
        System.out.println(hospitalDetailDto.getHosName());
        System.out.println("===================== Hospitalservice");


        return hospitalDetailDto;
    }

    public long maxNo(Hospital hospital){
        Long skj = hospitalRepository.findByHosNo(hospital);
//        System.out.println(skj);
        if (skj == null ) {
            skj = Long.valueOf(1);
        }else{
            skj+=1;
        }
        return (skj);
    }
    public long maxNoImg(HospitalImg hospitalImg){
        Long skj = hospitalImgRepository.findByMaxId(hospitalImg);
//        System.out.println(skj);
        if (skj == null ) {
            skj = Long.valueOf(1);
        }else{
            skj+=1;
        }
        return (skj);
    }

    public Hospital findByHosNoo(Long hosNo) {

        return hospitalRepository.findByHosNo(hosNo);
    }
//
//    public Long savedHospital(HospitalFormDto hospitalFormDto, List<MultipartFile> hospitalImgFileList) throws Exception{
//        // 01. 상품 등록
//        // 상품 등록 화면에서 넘어온 데이터를 이용하여 Item Entity를 생성합니다.
//        Hospital hospital = hospitalFormDto.createHospital();
//        hospitalRepository.save(hospital); // 상품 데이터 저장
//        System.out.println("병원 저장됨");
//        // 02. 상품에 들어가는 각 이미지 등록
//        for (int i = 0; i < hospitalImgFileList.size(); i++) {
//            HospitalImg hospitalImg = new HospitalImg();
//
//            // 해당 상품과 이미지와 연계를 맺어줌
//            // 실제 데이터 베이스에 item_id 컬럼에 동일한 값이 들어 갑니다.(참조 무결성 제약조건에 같은 값이 들어가야 한다는 소리)
//            hospitalImg.setHospital(hospital);
//
//            if(i==0){ // 1번째 이미지는 대표 이미지로 지정하기
//                hospitalImg.setRepImgYn("Y");
//            }else{
//                hospitalImg.setRepImgYn("N");
//            }
//            System.out.println(i +"번째 이미지 저장 전");
//            // 상품의 이미지 정보를 저장합니다.
//            hospitalImgService.saveHospitalImg(hospitalImg, hospitalImgFileList.get(i));
//            System.out.println(i +"번째 이미지 저장 후");
//
//        } // end for
//        return hospital.getHosNo();
//    }

    public void saveHospital(Hospital hospital, List<MultipartFile> hospitalImgFileList,HospitalFormDto hospitalFormDto) throws Exception {

        validateDuplicateHospital(hospital);
        if(hospital.getHosDAddress()==null){
            hospital.setHosDAddress(" ");
        }
        if(hospital.getHosPosY()==null){
            System.out.println(" 널인가");
            hospital.setHosPosY(0.0);
        }  if(hospital.getHosPosX()==null){
            hospital.setHosPosX(0.0);
        }
        // if문 분기처리
        if(hospital.getHosNo() == null){
            // hosNo가 null 이면
            hospital.setHosRegister(RegisterStatus.YES);
            Long maxNoo  = maxNo(hospital);
            //  maxNo()실행
            System.out.println("이거 여기는 말야 새로운 곳을 하는 곳인데 말야 너가 No 가 현재 null 인 상태야");
            System.out.println(maxNoo+"----------------------------------------");
            hospital.setHosNo(maxNoo);
            // insert구문이 들어간 repository 실행
            hospitalRepository.save(hospital);
            // 02. 상품에 들어가는 각 이미지 등록
            for (int i = 0; i < hospitalImgFileList.size(); i++) {
                HospitalImg hospitalImg = new HospitalImg();
                Long maxNoImg = maxNoImg(hospitalImg);
                hospitalImg.setId(maxNoImg);

                System.out.println("for 문 잘 들어오니?????!!!!!!!!!!");
                // 해당 상품과 이미지와 연계를 맺어줌
                // 실제 데이터 베이스에 item_id 컬럼에 동일한 값이 들어 갑니다.(참조 무결성 제약조건에 같은 값이 들어가야 한다는 소리)
                hospitalImg.setHospital(hospital);

                if(i==0){ // 1번째 이미지는 대표 이미지로 지정하기
                    hospitalImg.setRepImgYn("Y");
                }else{
                    hospitalImg.setRepImgYn("N");
                }
                System.out.println("병원 사진 정보"+i);
                System.out.println(hospitalImg);
                // 상품의 이미지 정보를 저장합니다.
                hospitalImgService.saveHospitalImg(hospitalImg, hospitalImgFileList.get(i));

            } // end for
        } else {
            // hosNO가 null이 아니면
            // update구문이 들어간 repository 실행
            int aa =hospitalRepository.updateHospital(hospital.getHosNo(),hospital.getHosId(),hospital.getHosPassword(),hospital.getHosDAddress(),hospital.getHosCeo(),hospital.getHosCeoEmail(),hospital.getHosSubject(),RegisterStatus.YES, hospital.getHosName(),hospital.getHosAddress(),hospital.getHosPostNum());
            System.out.println(aa+"   자아아아아아아아 여길 봐아ㅏㅏㅏ~~~~~!!!!!-----------");

            for (int i = 0; i < hospitalImgFileList.size(); i++) {
                HospitalImg hospitalImg = new HospitalImg();
                Long maxNoImg = maxNoImg(hospitalImg);
                hospitalImg.setId(maxNoImg);

                System.out.println("for 문 잘 들어오니?????!!!!!!!!!!");
                // 해당 상품과 이미지와 연계를 맺어줌
                // 실제 데이터 베이스에 item_id 컬럼에 동일한 값이 들어 갑니다.(참조 무결성 제약조건에 같은 값이 들어가야 한다는 소리)
                hospitalImg.setHospital(hospital);

                if(i==0){ // 1번째 이미지는 대표 이미지로 지정하기
                    hospitalImg.setRepImgYn("Y");
                }else{
                    hospitalImg.setRepImgYn("N");
                }
                System.out.println("병원 사진 정보"+i);
                System.out.println(hospitalImg);
                // 상품의 이미지 정보를 저장합니다.
                hospitalImgService.saveHospitalImg(hospitalImg, hospitalImgFileList.get(i));

            }


        }

    }

    private void validateDuplicateHospital(Hospital hospital) {
        Hospital findHospital = hospitalRepository.findByHosId(hospital.getHosId());
        if (findHospital != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        System.out.println("나는 서비스, 아직 레파지토리 다녀오기 전");
        Hospital hospital = hospitalRepository.findByHosId(id);
        System.out.println("나는 서비스, 레파지토리에서 아이디 있는지 확인하고 옴");
        if(hospital == null){
            throw new UsernameNotFoundException(id);
        }
        System.out.println(hospital.getHosId());
        return User.builder()
                .username(hospital.getHosId())
                .password(hospital.getHosPassword())
                .roles(hospital.getHosRegister().toString())
                .build();
    }
    @Transactional
    public Page<HospitalFormDto> findHospitalBy(HospitalSearchDto hospitalSearchDto, Pageable pageable){
        // 전체 병원 목록을 조회
        if(hospitalSearchDto.getSearchQuery()==null){
            hospitalSearchDto.setSearchQuery("");
        }
        List<Hospital> hospitals = hospitalRepository.findHospitalBy(hospitalSearchDto.getSearchQuery(), pageable);

        // 전체 병원 수를 조회
        Long totalCount = hospitalRepository.countHospital(hospitalSearchDto.getSearchQuery());
        // 병원 정보들을 저장하는 리스트 컬렉션
        System.out.println(totalCount);
        List<HospitalFormDto> hospitalFormDtos = new ArrayList<HospitalFormDto>();


        for(Hospital hospital: hospitals){
            // 병원 찾기 페이지에서 보여줄 dto 생성
            HospitalFormDto hospitalFormDto = new HospitalFormDto();

            // 병원 찾기 시 필요한 내용 dto저장(고유번호,주소,병원명,주소번호)
            hospitalFormDto.setHosNo(hospital.getHosNo());
            hospitalFormDto.setHosAddress(hospital.getHosAddress());
            hospitalFormDto.setHosName(hospital.getHosName());
            hospitalFormDto.setHosPostNum(hospital.getHosPostNum());

            // hospitalFormDtos에 hospitalForm 저장
            hospitalFormDtos.add(hospitalFormDto);
        }

        // 페이징 구현 객체를 반환
        return new PageImpl<HospitalFormDto>(hospitalFormDtos,pageable,totalCount);
    }

    public Long findHosNoByHosId(String hosId){
        Long hosNo = hospitalRepository.findHosNoByHosId(hosId);
        return hosNo;
    }
    public Hospital findByHosId(String hosId){
        Hospital hospital = hospitalRepository.findByHosId(hosId); // 병원 정보를 출력
        return hospital;
    }

    @Transactional(readOnly = true)
    public HospitalUpdateFormDto updateMyHospital(Long hosNo) {
        Hospital hospital = hospitalRepository.findById(hosNo)
                .orElseThrow(EntityNotFoundException::new);
        // 병원 이미지와 관련된 Entity 목록을 조회합니다.
        List<HospitalImg> hospitalImgList = hospitalImgRepository.findByHospitalOrderByIdAsc(hospital);

        // dto를 저장시킬 리스트 컬렉션
        List<HospitalImgDto> hospitalImgDtoList = new ArrayList<HospitalImgDto>();

        // 반복문을 사용하여 Entity를 Dto로 변경시켜 dto 컬렉션에 담습니다.
        for(HospitalImg hospitalImg :hospitalImgList){
            HospitalImgDto hospitalImgDto = HospitalImgDto.of(hospitalImg);
            hospitalImgDtoList.add(hospitalImgDto);
        }

        // 상품 Entity 정보를 구합니다.

        HospitalUpdateFormDto hospitalFormDto = HospitalUpdateFormDto.of(hospital);
        hospitalFormDto.setHospitalImgDtoList(hospitalImgDtoList);
        hospitalFormDto.setYImgUrl(hospitalImgDtoList.get(0).getImgUrl());

        return hospitalFormDto;
    }
//
//    // 화면(dto)에서 넘겨진 상품(item) 정보를 업데이트 합니다.
    private final HospitalImgService hospitalImgService;
    public Long updateHospital(@Valid HospitalUpdateFormDto hospitalFormDto, List<MultipartFile> hospitalImgFileList, PasswordEncoder passwordEncoder) throws Exception{
        Hospital hospital = hospitalRepository.findById(hospitalFormDto.getHosNo())
                .orElseThrow(EntityNotFoundException::new);

        hospital.updateHospital(hospitalFormDto,passwordEncoder); // 화면에서 넘어온 dto를 이용하여 item Entity에게 전달합니다.

        // 5개의 이미지에 대한 각각의 id 목록
        List<Long> hospitalImgIds = hospitalFormDto.getHospitalImgIds();

        // 각각의 상품 이미지 정보를 수정해줍니다.
        for (int i = 0; i < hospitalImgFileList.size(); i++) {
            hospitalImgService.updateHospitalImg(hospitalImgIds.get(i), hospitalImgFileList.get(i));
        }

        return hospital.getHosNo(); // 수정된 상품의 id를 반환합니다.
    }

}
