package cn.element.web.bind.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.PUT)
public @interface PutMapping {

//	/**
//	 * Alias for {@link RequestMapping#name}.
//	 */
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
