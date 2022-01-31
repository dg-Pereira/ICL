 def x = 1 in 
	def f = (fun y:Int -> y+x) in
		def g = (fun x:Int -> x + f(x))
			in g(2)
		end
	end
end ;;