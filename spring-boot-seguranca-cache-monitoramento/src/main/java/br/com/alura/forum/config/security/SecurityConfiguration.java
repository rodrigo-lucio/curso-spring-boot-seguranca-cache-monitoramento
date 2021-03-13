package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AutenticacaoService autenticacaoService;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Aqui conseguimos injetar, diferente do AutenticacaoViaTokenFilter 
	@Autowired
	private TokenService tokenService;	
	
	// Fazendo a injeção de AuthenticationManager durante a configuração
	// pois o spring não consegue fazer o @Autowired la no AutenticacaoController
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	/*
	 * Configurações de autenticação
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// BCryptPasswordEncoder = criptografia utilizada - criado metodo no fim da
		// classe para gerar uma senha
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}

	/*
	 * Configurações de autorização - URLS, o que é publico e privado
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/topicos").permitAll()
			.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
			.antMatchers(HttpMethod.POST, "/login").permitAll()
			.antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.csrf().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
	}

	/*
	 * Configurações de recursos estaticos(Java Script, CSS, Imagens)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring()
	        .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
	}
	/*
	 * Gerar uma senha BCryptPasswordEncoder public static void main(String[] args)
	 * { System.out.println(new BCryptPasswordEncoder().encode("123456")); }
	 */
}
