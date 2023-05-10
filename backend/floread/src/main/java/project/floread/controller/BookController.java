package project.floread.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import project.floread.model.Book;
import project.floread.service.BookService;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books/save")
    public String upload() {
        return "books-save";
    }


    @PostMapping("/books/save")
    public String create(@RequestPart("file") MultipartFile[] files,  Authentication authentication) throws IOException {

        //현재 로그인 중인 유저 userId가져오기
        String userId = authentication.getName();
        for (MultipartFile file : files) {
            if(file.isEmpty()) {
                System.out.println("파일 없음");
                return "index";
            }
            try {
                Book book = new Book();
                //원본 파일 이름 저장
                String sourceFileName = file.getOriginalFilename();
                //원본파일확장자면
                String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName);
                //확장자빼고
                String title = FilenameUtils.removeExtension(sourceFileName);

                //저장될 파일
                File destinationBook;
                String destinationBookName;
                String bookUrl = System.getProperty("user.dir")+"/../book/";
                System.out.println(bookUrl);
                try {

                    //파일명은 사용자auth2아이디_원본파일.txt로 저장됨
                    destinationBookName = title + '_' + userId + '.' + sourceFileNameExtension;

                    //파일 경로
                    destinationBook = new File(bookUrl + destinationBookName);

                    //부모디렉토리가 존재하지 않으면 생성
                    destinationBook.getParentFile().mkdirs();
                    //파일 이동
                    file.transferTo(destinationBook);

                    //데이터베이스에 저장
                    book.setFileName(destinationBookName);
                    book.setUrl(bookUrl + destinationBookName);
                    bookService.join(book, userId);

                } catch (IllegalStateException e) {
                    System.out.println("파일 존재");
                }

            } catch (IOException e) {

                System.out.println("저장 실패");
                return "index";
            }
        }
        return "index";
        //있을 경우 패스
    }


    //임시로 사용자의 책들을 출력
    @GetMapping("/read")
    public String Read(Authentication authentication) {
        String userId = authentication.getName();
        List<String> url = bookService.findUrl(userId);
        for (String s : url) {
            System.out.println(s);
            //파일 출력시 할 내용
            //File file = new File(s);
        }
        return "index";
    }
}
