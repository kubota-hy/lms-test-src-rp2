package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.time.Duration;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;


/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// TODO ここに追加
		goTo("http://localhost:8080/lms");
		getEvidence(new Object() {},"test01");
		String pageTitle = WebDriverUtils.webDriver.getTitle();
		assertEquals("ログイン | LMS", pageTitle);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA02");
		webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();

		//DOM構築前に検索するとエラーになるため、表示されるまで待機する
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		WebElement courseDetail = wait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector("ol.breadcrumb li.active")
						)
				);
		getEvidence(new Object() {},"test02");

		assertEquals("コース詳細", courseDetail.getText());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// TODO ここに追加
		WebElement functionMenu = webDriver.findElement(By.cssSelector("a.dropdown-toggle[role='button']"));
		functionMenu.click();

		WebDriverWait wait = new WebDriverWait(webDriver,Duration.ofSeconds(10));

		WebElement helpMenu = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//ul[contains(@class,'dropdown-menu')]//a[contains(text(),'ヘルプ')]")
						)
				);
		helpMenu.click();

		//タイトルは環境ごとに微妙に変わることがあるのでtitleContainsメソッドを利用
		wait.until(ExpectedConditions.titleContains("ヘルプ"));

		getEvidence(new Object() {}, "test03");

		assertTrue(webDriver.getTitle().contains("ヘルプ"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// TODO ここに追加
		WebElement faqLink = webDriver.findElement(By.linkText("よくある質問"));

		faqLink.click();

		//新しいタブへ切り替える
		//順番にアクセスする必要があるのでArrayList変換
		ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
		webDriver.switchTo().window(tabs.get(1));

		getEvidence(new Object() {},"test04");

		assertTrue(webDriver.getTitle().contains("よくある質問"));
	}
	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		// TODO ここに追加
		
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		// TODO ここに追加
	}

}
