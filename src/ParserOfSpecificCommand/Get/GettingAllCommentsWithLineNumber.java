package ParserOfSpecificCommand.Get;

import ParserOfSpecificCommand.Generated.ParseGettingAllComments;
import ParserOfSpecificCommand.Generated.ParseException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Satoshi Tanoue
 */
public class GettingAllCommentsWithLineNumber {

	//もっとわかりやすくしたいけどどうすれば？？？
	private ArrayList<String> comment = new ArrayList<>();
	//コメントが存在している行番号
	private ArrayList<Integer> keyValue = new ArrayList<>();
	//コメントが存在している行番号がキーで，その行番号に存在しているコメントの文字列．
	private HashMap <Integer,ArrayList<String>> map = new HashMap<>();

	private  int commentSize;

	//不適切なコメント
	//HashMapにコメントと行番号をhashmapにセットする．
	public GettingAllCommentsWithLineNumber(String inputString){
		int lineNumber = 0;
		String inputStrings[] = inputString.split("\n", -1);
		ParseGettingAllComments parser = new ParseGettingAllComments(new StringReader(inputString));
		try {
			//ソースコードからコメントを抽出．
			comment = parser.comment();
			//コメントがあるだけ繰り返す．
			for (int i = 0; i < comment.size(); i++) {
				if (!comment.get(i).isEmpty()) {//このif文　何？？？？？？
					String comments[] = comment.get(i).split("\n", -1);
					for (int j = 0; j < inputStrings.length; j++) {
						if(inputStrings[j].matches(".*"+Pattern.quote(comments[0])+".*")){
							lineNumber = j + 1;
							ArrayList<String> resultComment = new ArrayList<>();
							if(map.get(lineNumber) != null){
								resultComment = map.get(lineNumber);
							}
							resultComment.add(comment.get(i));
							keyValue.add(lineNumber);
							map.put(lineNumber, resultComment);
							inputStrings[j] = "";
							break;
						}
					}
				}
			}
			commentSize = comment.size();
		} catch (ParseException e) {
			System.out.println("コメントがありません");
			e.printStackTrace();
		}
	}

	public ArrayList<String> getComment() {
		return comment;
	}

	public ArrayList<Integer> getKeyValue() {
		return keyValue;
	}

	public HashMap<Integer, ArrayList<String>> getMap() {
		return map;

	}

	public int getCommentSize() {
		return commentSize;
	}
}

