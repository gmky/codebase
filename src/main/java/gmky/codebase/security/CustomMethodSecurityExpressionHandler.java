package gmky.codebase.security;

import gmky.codebase.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Method;
import java.util.function.Supplier;

@Slf4j
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    private final UserRepository userRepository;
    public CustomMethodSecurityExpressionHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, MethodInvocation mi) {
        MethodSecurityExpressionOperations root = this.createSecurityExpressionRoot(authentication.get(), mi);
        var ctx = new CustomMethodSecurityEvaluationContext(root, mi, this.getParameterNameDiscoverer());
        ctx.setBeanResolver(getBeanResolver());
        return ctx;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        var root = new CustomMethodSecurityExpressionRoot(authentication, userRepository);
        root.setDefaultRolePrefix(this.getDefaultRolePrefix());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }

    static class CustomMethodSecurityEvaluationContext extends MethodBasedEvaluationContext {

        CustomMethodSecurityEvaluationContext(MethodSecurityExpressionOperations root, MethodInvocation mi,
                                          ParameterNameDiscoverer parameterNameDiscoverer) {
            super(root, getSpecificMethod(mi), mi.getArguments(), parameterNameDiscoverer);
        }

        private static Method getSpecificMethod(MethodInvocation mi) {
            return AopUtils.getMostSpecificMethod(mi.getMethod(), AopProxyUtils.ultimateTargetClass(mi.getThis()));
        }
    }
}
