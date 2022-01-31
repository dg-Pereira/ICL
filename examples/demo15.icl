def comp = fun f:Int, g:Int -> fun x:Int -> f(g(x)) end end in
	def inc = fun x:Int -> x+1 end in
		def dup = comp(inc, inc) in
			print dup(2)
		end
	end
end ;;