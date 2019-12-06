activity_camera.xml                  은   layout폴더로

CameraActivity.java           는 소스코드

xml폴더는    res폴더에    drawable,mipmap,values 폴더랑 같은 위치

파일 내부에서  com.example.myapplication   은 현재 패키지로 바꾸셔야해여
그 뒤에붙는  .fileprovider는 놔두시구요

논리는  촬영버튼클릭->사진찍고 확인-> 이미지출력, 전송버튼클릭-> 메인액티비티로
바뀌면서 서버에는 uploads/사진저장   테스트라서 일단 제 서버로 했습니다
khsung0디비에는  test 테이블에 이름과 경로(상대경로)로 저장되게 했어여

preview관련 액티비티는 지우고 메인액티비티에서 카메라아이콘 클릭시 CameraActivity로 전환
되는 코드만 추가하면 될 것 같아여