package cn.element.web.bind.annotation;


import java.lang.annotation.*;

/**
 * Annotation for mapping HTTP {@code POST} requests onto specific handler
 * methods.
 *
 * <p>Specifically, {@code @PostMapping} is a <em>composed annotation</em> that
 * acts as a shortcut for {@code @RequestMapping(method = RequestMethod.POST)}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@RequestMapping(method = RequestMethod.POST)
public @interface PostMapping {

	/**
	 * Alias for {@link RequestMapping#name}.
	 */
//	@AliasFor(annotation = RequestMapping.class)
//	String name() default "";
//
//	/**
//	 * Alias for {@link RequestMapping#value}.
//	 */
//	@AliasFor(annotation = RequestMapping.class)
//	String[] value() default {};
//
//	/**
//	 * Alias for {@link RequestMapping#path}.
//	 */
//	@AliasFor(annotation = RequestMapping.class)
//	String[] path() default {};
//
//	/**
//	 * Alias for {@link RequestMapping#params}.
//	 */
//	@AliasFor(annotation = RequestMapping.class)
//	String[] params() default {};
//
//	/**
//	 * Alias for {@link RequestMapping#headers}.
//	 */
//	@AliasFor(annotation = RequestMapping.class)
//	String[] headers() default {};
//
//	/**
//	 * Alias for {@link RequestMapping#consumes}.
//	 */
//	@AliasFor(annotation = RequestMapping.class)
//	String[] consumes() default {};
//
//	/**
//	 * Alias for {@link RequestMapping#produces}.
//	 */
//	@AliasFor(annotation = RequestMapping.class)
//	String[] produces() default {};

}
