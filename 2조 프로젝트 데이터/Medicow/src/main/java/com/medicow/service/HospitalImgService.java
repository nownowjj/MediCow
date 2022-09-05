package com.medicow.service;

import com.medicow.model.entity.Hospital;
import com.medicow.model.entity.HospitalImg;
import com.medicow.repository.inter.HospitalImgRepository;
import com.medicow.repository.inter.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalImgService {

    @Value("${hospitalImgLocation}")
    private String hospitalImgLocation; // 상품 이미지가 업로드 되는 경로

    private final HospitalImgRepository hospitalImgRepository;
    private final FileService fileService;
    private final HospitalRepository hospitalRepository;

    //상품에 대한 이미지 정보를 저장해 줍니다.
    // FileServie 클래스는 이미지파일을 업로드, 삭제처리를 수행해주는 파일이었음 이 둘이 다름!
    public void saveHospitalImg(HospitalImg hospitalImg, MultipartFile hospitalImgFile) throws Exception{
        String oriImgName = hospitalImgFile.getOriginalFilename(); // 업로드 했던 이미지의 원본 파일 이름
        String imgName = ""; // uuid 형식의 저장된 이미지 이름
        String imgUrl =""; // 상품 이미지 불러오는 경로

        if(!StringUtils.isEmpty(oriImgName)){ // 원본 파일 이름이 있으면 업로드 하기
            imgName = fileService.uploadFile(hospitalImgLocation, oriImgName, hospitalImgFile.getBytes());
            System.out.println("============================================이미지 이름");
            System.out.println(imgName);
            System.out.println(oriImgName);
            imgUrl = "/images/hospital/"+imgName;
        }
        // 상품 이미지 정보를 저장합니다.
//        hospitalImg.setId(1L);
        System.out.println(oriImgName+"==============================================================");
        hospitalImg.updateHospitalImg(oriImgName, imgName, imgUrl);
        System.out.println(hospitalImg.getOriImgName()+"=====================================");

        hospitalImgRepository.save(hospitalImg);
    }

    public void updateHospitalImg(Long hospitalImgId, MultipartFile hospitalImgFile) throws Exception{
        if(!hospitalImgFile.isEmpty()){ // 업로드할 파일이 있으면
            HospitalImg savedHospitalImg = hospitalImgRepository.findById(hospitalImgId)
                    .orElseThrow(EntityNotFoundException::new);

            // 기존에 등록했던 옛날 이미지는 삭제 합니다.
            if(!StringUtils.isEmpty(savedHospitalImg.getImgName())){// 문자가 비어있느지 아닌지 체크하는게 StringUtils
                fileService.deleteFile(hospitalImgLocation+"/"+savedHospitalImg.getImgName());
            }
            String oriImgName = hospitalImgFile.getOriginalFilename(); //이미지 원본 이름

            String imgName=fileService.uploadFile(hospitalImgLocation, oriImgName, hospitalImgFile.getBytes());
            String imgUrl ="/images/hospital/"+imgName;

            // 상품 이미지 파일을 업로드합니다.
            savedHospitalImg.updateHospitalImg(oriImgName, imgName, imgUrl);

        }
    }

    public HospitalImg findYnHosImg(Hospital hosNo) {
        System.out.println("병원이미지서비스는 잘 오나?");
        HospitalImg hosImgUrl = hospitalImgRepository.findByHospitalAndRepImgYn(hosNo);
        return hosImgUrl;
    }

    public List<HospitalImg> getHosImg(Long hosNo) {
        Hospital hospital = hospitalRepository.findByHosNo(hosNo);
        List<HospitalImg> hospitalImgs = hospitalImgRepository.findByHospital(hospital);

        return hospitalImgs;
    }
}
