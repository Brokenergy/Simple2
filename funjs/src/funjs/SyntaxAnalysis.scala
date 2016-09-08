package funjs

import org.kiama.util.PositionedParserUtilities

/**
 * Module containing parsers for funjs.
 */
class SyntaxAnalysis extends PositionedParserUtilities {

    import FunJSTree._
    import scala.collection.immutable.Seq
    import scala.language.postfixOps

    lazy val program : PackratParser[Program] =
        exp ^^ Program

    lazy val exp : PackratParser[Exp] =
		exp ~ ("+" ~> term) ^^ { case e ~ t => PlusExp( e, t) } |
		exp ~ ("-" ~> term) ^^ { case e ~ t => SubExp( e, t) } |
		term
		
	lazy val term : PackratParser[Exp] =
		exp ~ ("*" ~> factor) ^^ { case t ~ f => StarExp( t, f) } |
		exp ~ ("/" ~> factor) ^^ { case t ~ f => SlashExp( t, f) } |
		factor
		

    lazy val factor : PackratParser[Exp] =
        app   |
        fun   |
        // FIXME //
		//where |//
        obj   |
        "false" ^^ (_ => BoolExp (false)) |
        "true" ^^ (_ => BoolExp (true)) |
        identifier ^^ {case t => IdnUse (t)} |
        integer ^^ (s => IntExp (s.toInt)) |
		exp ~> "->" ~> exp |
		
        "(" ~> exp <~ ")" |
        failure ("exp expected")
		

    // FIXME // 
	
	//lazy val where : PackratParser[Exp] =//
	
    
    lazy val obj : PackratParser[Exp] = 
        "{" ~> definitions <~ "}" ^^ {case ds => ObjExp(ds)}
		
	
		
    
    lazy val app : PackratParser[AppExp] =
        exp ~ exp ^^ {
            case f ~ arg => AppExp (f, arg)
        }
    lazy val fun : PackratParser[FunExp] = {
        ("fun" ~> ("(" ~> idndef <~ ")")) ~ ("{" ~> exp <~ "}") ^^ {case idn ~ body => FunExp (idn, body)}
    }

    lazy val definitions : PackratParser[List[Defn]] =
        rep1sep (defn, ";")

    lazy val defn : PackratParser[Defn] =
        idndef ~ (":" ~> exp) ^^ {
            case i ~ e => Defn (i, e)
        }

    // NOTE: You should not need to change anything below here...

    lazy val integer =
        regex ("[0-9]+".r)

    lazy val idndef =
        exp ~ identifier ^^ {case t ~ i => IdnDef (i,t)}

    val keywordStrings =
        Seq ("where", "else", "false", "if", "then", "true")

    lazy val keyword =
        keywords ("[^a-zA-Z0-9_]".r, keywordStrings)

    lazy val identifier =
        not (keyword) ~> identifierBase

    lazy val identifierBase =
        regex ("[a-zA-Z][a-zA-Z0-9_]*".r) |
        failure ("identifier expected")

    lazy val whitespaceParser : PackratParser[Any] =
        rep (whiteSpace | comment)

    lazy val comment : PackratParser[Any] =
        "/*" ~ rep (not ("*/") ~ (comment | any)) ~ "*/" |
        "//.*(\n|\\z)".r

}
