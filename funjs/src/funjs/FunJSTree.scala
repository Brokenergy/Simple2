package funjs

/**
 * Module containing tree structures for representing Moama programs.
 */
object FunJSTree {

    import org.kiama.attribution.Attributable

    /**
     * The common supertype of all source tree nodes.
     */
    sealed abstract class FunJSNode extends Attributable

    /**
     * A funjs program is an expression.
     */
    case class Program (exp : Exp) extends FunJSNode

    /**
     * Common class for all definitions.
     */
    case class Defn (idndef: IdnDef, body: Exp) extends FunJSNode

    /**
     * Base class of all types.
     */
    sealed abstract class Type

    /**
     * Boolean type
     */
    case class BoolType () extends Type

    /**
     * Function type
     */
    case class FunType (from : Type, to : Type) extends Type

    /**
     * Integer type
     */
    case class IntType () extends Type

    /**
     * Object Type
     **/
    case class ObjType () extends Type

    /**
     * An unknown type, for example, one belonging to a name that is not declared
     * but is used in an expression.
     */
    case class UnknownType (error: String) extends Type
    case class RedeclaredType (i: Identifier) extends Type
    case class TypeClash(i: Identifier, t1: Type, t2: Type) extends Type

    /**
     * Common superclass of expressions.
     */
    sealed abstract class Exp extends FunJSNode

    // FIXME: There are AST node types missing here //
    
	case class PlusExp (l : Exp, r : Exp) extends Exp
	
	case class StarExp (l : Exp, r : Exp) extends Exp
	
	case class SubExp (l : Exp, r : Exp) extends Exp
	
	case class SlashExp (l : Exp, r : Exp) extends Exp
	
	case class EqualToExp(l : Exp, r : Exp) extends Exp
	
	case class GreaterThanExp(l : Exp, r : Exp) extends Exp
	
	case class ObjExp(defns : List[Defn]) extends Exp
	
    /**
     * Application expression.
     */
    case class AppExp (fn : Exp, arg : Exp) extends Exp

     /**
     * Function argument and body.
     */
    case class FunExp (arg : IdnDef, body : Exp) extends Exp

   /**
     * Where expression.
     */
    case class WhereExp (exp : Exp, defns : List[Defn]) extends Exp

    /**
     * Boolean-valued expression (True or False).
     */
    case class BoolExp (b : Boolean) extends Exp

    /**
     * Integer-valued numeric expression.
     */
    case class IntExp (n : Int) extends Exp

    /**
     * An identifier reference.
     */
    sealed trait Idn extends FunJSNode {
        def idn : Identifier
    }

    /**
     * A defining occurrence (def) of an identifier.
     */
    case class IdnDef (idn : Identifier, tipe: Type) extends Idn

    /**
     * An applied occurrence (use) of an identifier.
     */
    case class IdnUse (idn : Identifier) extends Exp with Idn

    /**
     * A representation of identifiers as strings.
     */
    type Identifier = String

}
